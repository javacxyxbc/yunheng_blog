package com.moxi.hyblog.web.annotion.AuthorityVerify;

import com.moxi.hyblog.base.global.ECode;
import com.moxi.hyblog.utils.RedisUtil;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 权限校验
 *
 */

/**
 * @author hzh
 * @since 2020-08-07
 */
@Aspect
@Component
@Slf4j
public class AuthorityVerifyAspect {


    @Autowired
    AdminService adminService;

    @Autowired
    RedisUtil redisUtil;

    @Pointcut(value = "@annotation(authorityVerify)")
    public void pointcut(AuthorityVerify authorityVerify) {

    }

    @Around(value = "pointcut(authorityVerify)")
    public Object doAround(ProceedingJoinPoint joinPoint, AuthorityVerify authorityVerify) throws Throwable {
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        //读取token
        String token=request.getHeader(SysConf.Authorization);
        if(StringUtils.isEmpty(token)){
            return ResultUtil.result(ECode.UNAUTHORIZED, MessageConf.INVALID_TOKEN);
        }else{
            //放行
            String key= SysConf.USER_TOEKN + SysConf.REDIS_SEGMENTATION + token;
            if(StringUtils.isNotEmpty(key)&&redisUtil.hasKey(key)&&redisUtil.get(key).equals(token)){
                return joinPoint.proceed();
            }else{
                return ResultUtil.result(ECode.UNAUTHORIZED, MessageConf.OUT_SURVIVOR_TOKEN);
            }
        }
    }

}
