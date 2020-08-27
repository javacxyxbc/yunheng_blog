package com.moxi.hyblog.xo.utils;

import com.moxi.hyblog.xo.global.SysConf;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/24 21:21
 */
@Service
public class RabbitUtil {
    public static final  String EXCHANGE_DIRECT = "hyExchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDeleteBlogCommend(String type,String uids){
        Map<String,String> map =new HashMap<>();
        map.put(SysConf.COMMAND,type);
        map.put(SysConf.BLOG_UID,uids);
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT,SysConf.ROUTING_KEY_BLOG,map);
    }
}
