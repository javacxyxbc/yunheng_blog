package com.moxi.hyblog.commons.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.base.enums.EPublish;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.commons.config.feign.FeignConfiguration;
import com.moxi.hyblog.commons.entity.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hzh
 * @since 2020-08-07
 */

@FeignClient(name = "hy-admin",configuration = FeignConfiguration.class)
public interface AdminFeignClient {


    /**
     * 获取系统配置信息
     */
    @RequestMapping(value = "/systemConfig/getSystemConfig", method = RequestMethod.GET)
    public String getSystemConfig();

    @GetMapping("/blog/getBlogUIds")
    public List<Blog> getBlogUIds( );
    /**
     * 跟新博客的点赞,点击数
     */
    @GetMapping("/blog/updateBlogCollect")
    public void updateBlogCollect(@RequestParam("list")List<Blog> list);
}