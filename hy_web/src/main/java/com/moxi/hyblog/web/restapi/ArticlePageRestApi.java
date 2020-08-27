package com.moxi.hyblog.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.base.enums.EPublish;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.global.BaseSysConf;
import com.moxi.hyblog.commons.config.jwt.Audience;
import com.moxi.hyblog.commons.config.jwt.JwtHelper;
import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.utils.*;
import com.moxi.hyblog.web.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.hyblog.web.annotion.collectAfter.Collect;
import com.moxi.hyblog.web.utils.RabbitMqUtil;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SQLConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.service.BlogService;
import com.moxi.hyblog.xo.utils.WebUtil;
import com.netflix.client.http.HttpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/23 14:58
 */
@RestController
@RequestMapping(value = "/articlePage")
@Api(value = "文章页面接口", tags = {"文章页面相关接口"})
@Slf4j
public class ArticlePageRestApi {



    @Autowired
    BlogService blogService;
    @Autowired
    RabbitMqUtil rabbitMqUtil;
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    private Audience audience;
    //前端接口


    @ApiOperation(value = "根据文章uid获取文章的相关信息", notes = "根据文章uid获取文章的相关信息", response = String.class)
    @GetMapping("/get")
    public String getByUid(@RequestParam("uid")String uid) {
        if(StringUtils.isEmpty(uid)){
            return ResultUtil.result(com.moxi.hyblog.xo.global.SysConf.ERROR, MessageConf.UID_CANNOT_BLANK);
        }
        String key=SysConf.BLOG+SysConf.REDIS_SEGMENTATION+uid;
        if(redisUtil.hasKey(key)){
            //如果redis中有数据
            Map<Object, Object> blog = redisUtil.hGetAll(key);
            //发送到rabbitmq进行统计
            sendClickToRabbit(uid);
            return ResultUtil.result(SysConf.SUCCESS,blog);
        }else{
            //从mysql读取,缓存到redis
            Map blog=blogService.getBlogByIdAsMap(uid);
            //缓存
            redisUtil.hPutAll(key,blog);
            //设置过期时间
            redisUtil.expire(key,1, TimeUnit.DAYS);
            //发送到rabbitmq进行统计
            sendClickToRabbit(uid);
            return ResultUtil.result(SysConf.SUCCESS,blog);
        }
    }


   //feign接口
   @ApiOperation(value = "根据文章uid获取文章的相关信息", notes = "根据文章uid获取文章的相关信息", response = String.class)
   @GetMapping("/Fget")
   public String FgetByUid(@RequestParam("uid")String uid) {
       if(StringUtils.isEmpty(uid)){
           return ResultUtil.result(com.moxi.hyblog.xo.global.SysConf.ERROR, MessageConf.UID_CANNOT_BLANK);
       }
       return ResultUtil.result(SysConf.SUCCESS,blogService.getBlogById(uid));
   }

    @ApiOperation(value = "推荐文章列表", notes = "推荐文章列表", response = String.class)
    @GetMapping("/RecommendArticles")
    public String RecommendArticles(@RequestParam("uid")String uid) {
        if(StringUtils.isEmpty(uid)){
            return ResultUtil.result(com.moxi.hyblog.xo.global.SysConf.ERROR, MessageConf.UID_CANNOT_BLANK);
        }
        return ResultUtil.result(SysConf.SUCCESS,blogService.getRecommendBlogList(uid));
    }
    @ApiOperation(value = "推荐分类列表", notes = "推荐分类列表", response = String.class)
    @GetMapping("/RecommendSorts")
    public String RecommendSorts() {
        return ResultUtil.result(SysConf.SUCCESS,blogService.getRecommendSorts());
    }


    /**
     * 点赞后发送统计消息到rabbtimq
     */

    @AuthorityVerify
    @ApiOperation(value = "通过uid给博客点赞", notes = "通过uid给博客点赞", response = String.class)
    @PostMapping("/priseBlogByUid")
    public String priseBlogByUid(@RequestParam("uid")String uid,@RequestParam("token")String token) {
        String userUid=jwtHelper.parseAndGet(token,audience.getBase64Secret(),SysConf.ADMIN_UID);
        if(StringUtils.isNotEmpty(userUid)&&StringUtils.isNotEmpty(uid)){
            if(blogService.priseBlog(uid,userUid)){
                //发送点赞到rabbitmq
                sendPriseToRabbit(uid);
                return ResultUtil.result(SysConf.SUCCESS,"点赞成功");
            }else{
                return ResultUtil.result(SysConf.ERROR,"您已经点过赞了");
            }
        }else{
            return ResultUtil.result(SysConf.ERROR,"传入参数有误");
        }
    }



    @Async
    public  void sendClickToRabbit(String uid){
        rabbitMqUtil.sendCommentCollect(uid, BaseSysConf.CLICK);
    }

    @Async
    public void sendPriseToRabbit(String uid){
        rabbitMqUtil.sendCommentCollect(uid, SysConf.PRISE);
    }
}
