package com.moxi.hyblog.commons.feign;

import com.moxi.hyblog.commons.config.feign.FeignConfiguration;
import com.moxi.hyblog.commons.entity.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author hzh
 * @since 2020-08-07
 */
@FeignClient(name = "hy-search",configuration = FeignConfiguration.class)
public interface SearchFeignClient {

    /**
     * ElasticSearch添加博客
     *
     * @param Eblog
     * @return
     */
    @PostMapping("/search/addBlogbyUid")
    public String addBlogIndex(@RequestBody Blog Eblog);

    /**
     * 通过ElasticSearch删除博客
     *
     * @param uid
     * @return
     */
    @PostMapping("/search/delBlogbyUid")
    public String delBlog(@RequestParam(required = true) String uid);

    /**
     * 初始化ElasticSearch索引
     *
     * @return
     */
    @PostMapping("/search/initElasticSearchIndex")
    public String initElasticSearchIndex();

    /**
     * 初始化Solr索引
     *
     * @return
     */
    @PostMapping("/search/initSolrIndex")
    public String initSolrIndex();


}
