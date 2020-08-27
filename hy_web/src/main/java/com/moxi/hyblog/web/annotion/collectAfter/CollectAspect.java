//package com.moxi.hyblog.web.annotion.collectAfter;
//
//import com.moxi.hyblog.base.global.BaseSysConf;
//import com.moxi.hyblog.commons.entity.Blog;
//import com.moxi.hyblog.utils.WebUtils;
//import com.moxi.hyblog.web.global.MethodConf;
//import com.moxi.hyblog.web.utils.RabbitMqUtil;
//import com.moxi.hyblog.xo.global.SysConf;
//import com.netflix.client.http.HttpRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
///**
// * @author hzh
// * @version 1.0
// * @date 2020/8/24 11:42
// * 文章点击,点赞后的切面--发送消息到rabbitmq
// */
//@Aspect
//@Component
//@Slf4j
//public class CollectAspect {
//
//    @Autowired
//    RabbitMqUtil rabbitMqUtil;
//    @Pointcut(value = "@annotation(collect)")
//    public void pointcut(Collect collect){
//
//    }
//    @AfterReturning(pointcut = "pointcut(collect)",returning = "rvt")
//    public void  afterReturn(JoinPoint point,Object rvt,Collect collect) throws Throwable{
//        String methodName=point.getSignature().getName();
//        //获取返回的值
//        Map blog=WebUtils.getData(rvt.toString(), Map.class);
//        //获取返回的code
//        String code=WebUtils.getCode(rvt.toString());
//        //如果是成功类型
//        if(code.equals("success")){
//            switch (methodName){
//                //点击博客的行为
//                case MethodConf.GET_BLOG_BY_UID:{
//                    String uid= (String) blog.get(SysConf.UID);
//                    if(uid!=null){
//                       //发送到rabbitmq
//                        sendClickToRabbit(uid);
//                    }
//                    break;
//                }
//                //点赞博客的行为
//                case MethodConf.PRISE_BLOG:{
//                    String uid= (String) blog.get(SysConf.UID);
//                    if(uid!=null){
//                        //发送到rabbitmq
//                        sendPriseToRabbit(uid);
//                    }
//                    break;
//                }
//            }
//        }
//    }
//
//
//
//    public  void sendClickToRabbit(String uid){
//        rabbitMqUtil.sendCommentCollect(uid, BaseSysConf.CLICK);
//    }
//
//    public void sendPriseToRabbit(String uid){
//        rabbitMqUtil.sendCommentCollect(uid, SysConf.PRISE);
//    }
//}
