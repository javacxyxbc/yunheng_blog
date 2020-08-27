package com.moxi.hyblog.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.global.ECode;
import com.moxi.hyblog.commons.config.jwt.Audience;
import com.moxi.hyblog.commons.config.jwt.JwtHelper;
import com.moxi.hyblog.commons.entity.User;
import com.moxi.hyblog.utils.*;
import com.moxi.hyblog.web.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SQLConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.service.UserService;
import com.moxi.hyblog.xo.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/25 15:28
 */
@RestController
@RequestMapping(value = "/user")
@Api(value = "用户相关接口", tags = {"用户相关接口"})
@Slf4j
public class UserRestApi {
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    private Audience audience;
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${file.reflect.path}")
    String reflectPath;

    @AuthorityVerify
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", response = String.class)
    @GetMapping("/get")
    public String get(@RequestParam("token")String token) {
        if(StringUtils.isEmpty(token)){
            return ResultUtil.result(ECode.UNAUTHORIZED, MessageConf.INVALID_TOKEN);
        }
        //获取用户uid
        Claims claims = jwtHelper.parseJWT(token, audience.getBase64Secret());
        String uid=(String) claims.get(SysConf.ADMIN_UID);

        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq(SysConf.UID,uid);
        wrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        User user=userService.getOne(wrapper);
        if(user!=null){
            user.setPassWord("");
            return ResultUtil.result(SysConf.SUCCESS,user);
        }else{
            return ResultUtil.result(SysConf.ERROR,MessageConf.USER_NO_FOUND);
        }
    }

    @AuthorityVerify
    @ApiOperation(value = "编辑用户信息", notes = "编辑用户信息")
    @PostMapping("/editUser")
    public String editUser(HttpServletRequest request, @RequestBody UserVO userVO) {
        String userUid = userVO.getUid();
        User user = userService.getById(userUid);
        user.setNickName(userVO.getNickName());
        user.setAvatar(userVO.getAvatar());
        user.setBirthday(userVO.getBirthday());
        user.setSummary(userVO.getSummary());
        user.setGender(userVO.getGender());
        user.setQqNumber(userVO.getQqNumber());
        user.setEmail(userVO.getEmail());
        user.updateById();
        user.setPassWord("");
        return ResultUtil.result(SysConf.SUCCESS,user);
    }
}
