package com.moxi.hyblog.web.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.base.enums.EPublish;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.utils.RedisUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.web.global.SQLConf;
import com.moxi.hyblog.web.utils.RabbitMqUtil;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.mapper.BlogMapper;
import com.moxi.hyblog.xo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/24 15:18
 */

/**
 * 每天24点的定时任务,同步redis中的数据到mysql
 */
@Component
public class collectSchedule {
    @Autowired
    RabbitMqUtil rabbitMqUtil;
    @Autowired
    BlogService blogService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 定时任务一:跟新这一天,被访问过的博客的访客量和点赞量到Mysql中,redis中的数据结构是hash
     */
    @Scheduled(cron = "59 59 23 * * *")
    public void FixedTimeCollect(){
        System.out.println("begin-----");
        //先获取博客uids,及对应的clickCount,priseCount
        List<Blog> blogList=blogService.getBlogUids();
        List<Blog> list=new ArrayList<>();//用于存放需要跟新的blog
        //2.读取redis中的点赞,点击数
        for(Blog blog:blogList){
            Boolean flag=false;
            if(StringUtils.isNotEmpty(blog.getUid())){
                String key=SysConf.BLOG+SysConf.REDIS_SEGMENTATION+blog.getUid();
                if(redisUtil.hasKey(key)){
                    int clickCount= (int) redisUtil.hGet(key,SQLConf.CLICK_CNT);
                    int priseCount= (int) redisUtil.hGet(key,SQLConf.PRISE_CNT);
                    if(clickCount>blog.getClickCount()){
                        flag=true;
                        blog.setClickCount(clickCount);
                    }
                    if(priseCount>blog.getPriseCount()){
                        flag=true;
                        blog.setPriseCount(priseCount);
                    }
                    if(flag){
                        list.add(blog);
                    }
                }
            }
        }
        //3.批量跟新
       if(list.size()>0){
           blogService.updateCollectBatch(list);
       }
    }
}
