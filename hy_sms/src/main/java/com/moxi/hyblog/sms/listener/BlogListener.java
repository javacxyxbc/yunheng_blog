package com.moxi.hyblog.sms.listener;

import com.moxi.hyblog.sms.global.SysConf;
import com.moxi.hyblog.sms.feign.SearchFeignClient;

import com.moxi.hyblog.utils.JsonUtils;
import com.moxi.hyblog.utils.RedisUtil;
import com.moxi.hyblog.utils.ServerInfo.Sys;
import com.moxi.hyblog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 博客监听器(用于更新Redis和索引)
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Component
@Slf4j
public class BlogListener {


    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    RedisUtil redisUtil;


    @RabbitListener(queues = "hyBlog")
    public void update(Map<String, String> map) {
        if (map != null) {

            String comment = map.get(SysConf.COMMAND);

            String uid = map.get(SysConf.BLOG_UID);

            switch (comment) {
                case SysConf.ADD: {
                    log.info("hy-sms处理增加博客");
                    // 增加ES索引
                    searchFeignClient.addElasticSearchIndexByUid(uid);

                }
                break;
                case SysConf.DELETE_BATCH: {

                    log.info("hy-sms处理批量删除博客");
                    // 删除ElasticSearch博客索引
                    searchFeignClient.deleteElasticSearchByUids(uid);
                    //删除redis缓存
                    deleteBatchRedis(uid);

                }
                break;

                case SysConf.DELETE: {
                    log.info("hy-sms处理删除博客： uid=" + uid);


                    // 删除ES索引
                    searchFeignClient.deleteElasticSearchByUid(uid);

                    //删除redis缓存
                    deleteBatchRedis(uid);
                }
                break;
                case SysConf.EDIT: {
                    log.info("hy-sms处理编辑博客");

                    // 更新ES索引
                    searchFeignClient.addElasticSearchIndexByUid(uid);
                    //删除redis缓存
                    deleteBatchRedis(uid);
                }
                break;

            }
        }
    }


    public void deleteBatchRedis(String uids){
        //删除redis中缓存
        List<String> uidList = StringUtils.changeStringToString(uids, SysConf.FILE_SEGMENTATION);
        List<String> deleteList=new ArrayList<>();
        for(String id:uidList){
            deleteList.add(SysConf.BLOG+SysConf.REDIS_SEGMENTATION+id);
        }
        redisUtil.delete(deleteList);
    }

}
