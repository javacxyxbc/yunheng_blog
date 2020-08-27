package com.moxi.hyblog.web.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.exception.ThrowableUtils;
import com.moxi.hyblog.base.holder.RequestHolder;
import com.moxi.hyblog.base.validator.group.GetOne;
import com.moxi.hyblog.base.validator.group.Insert;
import com.moxi.hyblog.commons.config.jwt.Audience;
import com.moxi.hyblog.commons.config.jwt.JwtHelper;
import com.moxi.hyblog.commons.entity.User;
import com.moxi.hyblog.commons.feign.PictureFeignClient;
import com.moxi.hyblog.utils.*;
import com.moxi.hyblog.web.global.MessageConf;
import com.moxi.hyblog.web.global.RedisConf;
import com.moxi.hyblog.web.global.SQLConf;
import com.moxi.hyblog.web.global.SysConf;
import com.moxi.hyblog.web.utils.RabbitMqUtil;
import com.moxi.hyblog.xo.service.UserService;
import com.moxi.hyblog.xo.utils.WebUtil;
import com.moxi.hyblog.xo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户登录RestApi，系统自带的登录注册功能
 * 第三方登录请移步AuthRestApi
 * </p>
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/login")
@Api(value = "登录管理相关接口", tags = {"loginRestApi"})
@Slf4j
public class LoginRestApi {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    RabbitMqUtil rabbitMqUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    PictureFeignClient pictureFeignClient;
    @Autowired
    WebUtil webUtil;
    @Value(value = "${BLOG.USER_TOKEN_SURVIVAL_TIME}")
    private Long userTokenSurvivalTime;
    @Autowired
    private Audience audience;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public String login(@RequestParam("userName")String userName,@RequestParam("passWord")String passWord) {
        //查询用户是用邮箱,用户名,还是手机号登入,
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(SQLConf.USER_NAME, userName).or().eq(SQLConf.EMAIL, userName)
        .or().eq(SQLConf.MOBILE,userName));
        queryWrapper.last("limit 1");
        User user = userService.getOne(queryWrapper);

        if (user == null || EStatus.DISABLED == user.getStatus()) {
            return ResultUtil.result(SysConf.ERROR, "用户不存在");
        }

        if (EStatus.FREEZE == user.getStatus()) {
            return ResultUtil.result(SysConf.ERROR, "用户账号未激活");
        }

        if (StringUtils.isNotEmpty(user.getPassWord()) && user.getPassWord().equals(MD5Utils.string2MD5(passWord))) {
            // 更新登录信息
            HttpServletRequest request = RequestHolder.getRequest();
            String ip = IpUtils.getIpAddr(request);
            Map<String, String> userMap = IpUtils.getOsAndBrowserInfo(request);
            user.setBrowser(userMap.get(SysConf.BROWSER));
            user.setOs(userMap.get(SysConf.OS));
            user.setLastLoginIp(ip);
            user.setLastLoginTime(new Date());
            user.updateById();
            //创建token
            String jwtToken = jwtHelper.createJWT(user.getUserName(),
                    user.getUid(),
                    SysConf.USER,
                    audience.getClientId(),
                    audience.getName(),
                    audience.getExpiresSecond() * 1000,
                    audience.getBase64Secret());
            // 过滤密码
            user.setPassWord("");

            Map<String,Object> map=new HashMap<>();
            map.put(SysConf.USER_UID,user.getUid());
            map.put(SysConf.TOKEN,jwtToken);
            //将从数据库查询的数据缓存到redis中
            redisUtil.setEx(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + jwtToken, jwtToken, userTokenSurvivalTime, TimeUnit.HOURS);
            return ResultUtil.result(SysConf.SUCCESS, map);
        } else {
            return ResultUtil.result(SysConf.ERROR, "账号或密码错误");
        }
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping("/register")
    public String register(@RequestParam("userName")String userName,@RequestParam("passWord")String passWord) {
        if(userName.length() < 5 || userName.length() >= 20 ||passWord.length() < 5 ||passWord.length() >= 20) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        HttpServletRequest request = RequestHolder.getRequest();
        String ip = IpUtils.getIpAddr(request);
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(SQLConf.USER_NAME, userName).or().eq(SQLConf.EMAIL, userName).eq(SQLConf.MOBILE,userName));
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            return ResultUtil.result(SysConf.ERROR, "用户已存在");
        }
        user = new User();
        user.setUserName(userName);
        user.setNickName(userName);
        user.setPassWord(MD5Utils.string2MD5(passWord));
        // 设置账号来源
        user.setLastLoginIp(ip);
        user.setBrowser(map.get(SysConf.BROWSER));
        user.setOs(map.get(SysConf.OS));
        user.setStatus(EStatus.ENABLE);
        user.insert();


        //将从数据库查询的数据缓存到redis中，用于用户邮箱激活，1小时后过期
        //redisUtil.setEx(RedisConf.ACTIVATE_USER + RedisConf.SEGMENTATION + jwtToken, JsonUtils.objectToJson(user), 1, TimeUnit.HOURS);

        // 发送邮件，进行账号激活
        //rabbitMqUtil.sendActivateEmail(user, jwtToken);

        return ResultUtil.result(SysConf.SUCCESS, "注册成功，请登录");
    }

    @ApiOperation(value = "激活用户账号", notes = "激活用户账号")
    @GetMapping("/activeUser/{token}")
    public String bindUserEmail(@PathVariable("token") String token) {
        // 从redis中获取用户信息
        String userInfo = redisUtil.get(RedisConf.ACTIVATE_USER + RedisConf.SEGMENTATION + token);
        if (StringUtils.isEmpty(userInfo)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INVALID_TOKEN);
        }
        User user = JsonUtils.jsonToPojo(userInfo, User.class);
        if (EStatus.FREEZE != user.getStatus()) {
            return ResultUtil.result(SysConf.ERROR, "用户账号已经被激活");
        }
        user.setStatus(EStatus.ENABLE);
        user.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public String logout(@ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {
        if (StringUtils.isEmpty(token)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.OPERATION_FAIL);
        }
        redisUtil.delete(SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + token);
        return ResultUtil.result(SysConf.SUCCESS, "退出成功");
    }

}
