package com.moxi.hyblog.admin.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.moxi.hyblog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.hyblog.admin.global.SQLConf;
import com.moxi.hyblog.admin.global.SysConf;
import com.moxi.hyblog.commons.config.jwt.Audience;
import com.moxi.hyblog.commons.config.jwt.JwtHelper;
import com.moxi.hyblog.commons.entity.Admin;
import com.moxi.hyblog.utils.CheckUtils;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 需放行接口RestApi
 * </p>
 *
 * 1.ok--8/9
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/auth")
@Api(value = "登录相关接口", tags = {"登录相关接口"})
@Slf4j
public class AuthRestApi {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private Audience audience;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @OperationLogger(value = "注册管理员")
    @ApiOperation(value = "注册管理员", notes = "注册管理员")
    @PostMapping("/register")
    //1.验证格式
    //2.验证验证码
    //3.验证是否已经存在(uername,email,phone)
    //4.密码加密
    //5.清楚缓存
    public String register(HttpServletRequest request,
                           @ApiParam(name = "assignbody", value = "管理员注册对象", required = true) @RequestBody(required = true) Admin registered) {

        String mobile = registered.getMobile();
        String userName = registered.getUserName();
        String email = registered.getEmail();
        String passWord = registered.getPassWord();
        String code = registered.getValidCode();
        String validCode = null;

        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(passWord)) {
            return ResultUtil.result(SysConf.ERROR, "用户名或密码不能为空");
        }

        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
            return ResultUtil.result(SysConf.ERROR, "邮箱和手机号至少一项不能为空");
        }

        //手机号为空时为邮箱注册 从redis中获取验证码
        if (StringUtils.isEmpty(mobile) && CheckUtils.checkEmail(email)) {
            validCode = stringRedisTemplate.opsForValue().get(email);
        } else if (StringUtils.isEmpty(email) && CheckUtils.checkMobileNumber(mobile)) {
            validCode = stringRedisTemplate.opsForValue().get(mobile);
        } else {
            return ResultUtil.result(SysConf.ERROR, "邮箱或手机号格式有误");
        }
        if (validCode.isEmpty()) {
            return ResultUtil.result(SysConf.ERROR, "验证码已过期");
        }

        if (code.equals(validCode)) {
            return ResultUtil.result(SysConf.ERROR, "验证码不正确");
        }


        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, userName);
        Admin admin = adminService.getOne(queryWrapper);

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        if (admin == null) {
            if (StringUtils.isNotEmpty(email)) {
                wrapper.eq(SQLConf.EMAIL, email);
            } else {
                wrapper.eq(SQLConf.MOBILE, mobile);
            }

            if (adminService.getOne(wrapper) != null) {
                return ResultUtil.result(SysConf.ERROR, "管理员账户已存在");
            }

            registered.setStatus(0);//设置为未审核状态
            //密码加密
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            registered.setPassWord(encoder.encode(registered.getPassWord()));
            adminService.save(registered);
            //清除redis中的缓存
            if (StringUtils.isEmpty(mobile)) {
                stringRedisTemplate.delete(email);
            } else {
                stringRedisTemplate.delete(mobile);
            }
            return ResultUtil.result(SysConf.SUCCESS, "注册成功");
        }
        return ResultUtil.result(SysConf.ERROR, "管理员账户已存在");
    }

    @OperationLogger(value = "更新管理员密码")
    @ApiOperation(value = "更新管理员密码", notes = "更新管理员密码")
    @PostMapping("/updatePassWord")
    //1.解析数据
    //2.验证管理员是否存在,密码是否为空
    //3.更新密码
    public String updatePassWord(HttpServletRequest request,
                                 @ApiParam(name = "userInfo", value = "管理员账户名", required = true) @RequestParam(name = "userInfo", required = true) String userInfo,
                                 @ApiParam(name = "passWord", value = "管理员旧密码", required = true) @RequestParam(name = "passWord", required = true) String passWord,
                                 @ApiParam(name = "newPassWord", value = "管理员新密码", required = true) @RequestParam(name = "newPassWord", required = true) String newPassWord) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (CheckUtils.checkEmail(userInfo)) {
            queryWrapper.eq(SQLConf.EMAIL, userInfo);
        } else if (CheckUtils.checkMobileNumber(userInfo)) {
            queryWrapper.eq(SQLConf.MOBILE, userInfo);
        } else {
            queryWrapper.eq(SQLConf.USER_NAME, userInfo);
        }
        Admin admin = adminService.getOne(queryWrapper);
        if (admin == null) {
            return ResultUtil.result(SysConf.ERROR, "管理员不存在");
        }
        if (StringUtils.isEmpty(passWord)) {
            return ResultUtil.result(SysConf.ERROR, "旧密码不能为空");
        }
        if (StringUtils.isEmpty(newPassWord)) {
            return ResultUtil.result(SysConf.ERROR, "新密码不能为空");
        }
        String uid = admin.getUid();

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(passWord, admin.getPassWord());
        if (isPassword) {
            admin.setPassWord(encoder.encode(newPassWord));
            UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(SQLConf.UID, uid);
            admin.setUpdateTime(new Date());
            adminService.update(admin, updateWrapper);
            return ResultUtil.result(SysConf.SUCCESS, "密码更新成功");
        }
        return ResultUtil.result(SysConf.ERROR, "旧密码错误");
    }

    @OperationLogger(value = "更新token")
    @ApiOperation(value = "更新token", notes = "更新token")
    @PostMapping("/refreshToken")
    public String refreshToken(String oldToken) {

        final String token = oldToken.substring(tokenHead.length());
        if (jwtHelper.canTokenBeRefreshed(token, audience.getBase64Secret())) {
            return jwtHelper.refreshToken(token, audience.getBase64Secret(), audience.getExpiresSecond());
        }
        return null;
    }
}
