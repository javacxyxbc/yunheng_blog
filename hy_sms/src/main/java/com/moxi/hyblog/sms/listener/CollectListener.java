package com.moxi.hyblog.sms.listener;

import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.sms.feign.WebFeignClient;
import com.moxi.hyblog.sms.global.SqlConf;
import com.moxi.hyblog.sms.global.SysConf;
import com.moxi.hyblog.utils.RedisUtil;
import com.moxi.hyblog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/23 21:36
 */
@Component
@Slf4j
public class CollectListener {

    @Resource
    RedisUtil redisUtil;

    @Autowired
    WebFeignClient webFeignClient;

    @RabbitListener(queues = "hyCollect")
    public void listenCollect(Map<String,String> map){
        String commend=map.get(SysConf.COMMAND);
        switch (commend){
            case SysConf.CLICK:{
//                浏览
                String uid=map.get(SysConf.UID);
                String key=SysConf.BLOG+SysConf.REDIS_SEGMENTATION+uid;
                if(redisUtil.hasKey(key)){
                    redisUtil.hIncrBy(key,SqlConf.CLICK_CNT,10);
                }
                break;
            }
            case SysConf.PRISE:{
//                点赞
                String uid=map.get(SysConf.UID);
                String key=SysConf.BLOG+SysConf.REDIS_SEGMENTATION+uid;
                if(redisUtil.hasKey(key)){
                    redisUtil.hIncrBy(key, SqlConf.PRISE_CNT,10);
                }
                break;
            }
        }

    }
}
