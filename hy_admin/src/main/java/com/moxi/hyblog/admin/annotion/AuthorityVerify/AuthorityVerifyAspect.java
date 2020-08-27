package com.moxi.hyblog.admin.annotion.AuthorityVerify;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.admin.global.MessageConf;
import com.moxi.hyblog.admin.global.RedisConf;
import com.moxi.hyblog.admin.global.SQLConf;
import com.moxi.hyblog.admin.global.SysConf;
import com.moxi.hyblog.commons.entity.Admin;
import com.moxi.hyblog.commons.entity.CategoryMenu;
import com.moxi.hyblog.commons.entity.Role;
import com.moxi.hyblog.utils.JsonUtils;
import com.moxi.hyblog.utils.RedisUtil;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.service.AdminService;
import com.moxi.hyblog.xo.service.CategoryMenuService;
import com.moxi.hyblog.xo.service.RoleService;
import com.moxi.hyblog.base.enums.EMenuType;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.global.ECode;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 权限校验 切面实现
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

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attribute.getRequest();

        //获取请求路径
        String url = request.getRequestURI();

        // 解析出请求者的ID和用户名
        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();
        String key=RedisConf.ADMIN_VISIT_MENU + SysConf.REDIS_SEGMENTATION + adminUid;



        if(!redisUtil.hasKey(key)){
            // redis中还没有没有该管理员的权限列表,就查询mysql数据库获取,并保存到redis set集合中

            String[] urlList= adminService.getAdminAuthorities(adminUid);
            for(int i=0;i<urlList.length;i++){
                System.out.println(urlList[i]);
            }
            //保存权限列表到redis set集合中
            redisUtil.sAdd(key,urlList);
            redisUtil.expire(key,1,TimeUnit.HOURS);
        }

        // 判断该角色是否能够访问该接口,即url是否为set集合的成员
        if(!redisUtil.sIsMember(key,url)){
            System.out.println("用户不具有操作权限，访问的路径: "+url );
            return ResultUtil.result(ECode.NO_OPERATION_AUTHORITY, MessageConf.RESTAPI_NO_PRIVILEGE);
        }else{
            System.out.println("用户具有访问权限:"+url);
        }

        //执行业务
        return joinPoint.proceed();
    }

}
