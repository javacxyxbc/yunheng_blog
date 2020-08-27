package com.moxi.hyblog.search.service;

import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.commons.entity.Tag;
import com.moxi.hyblog.search.global.SysConf;
import com.moxi.hyblog.search.mapper.HighlightResultHelper;
import com.moxi.hyblog.search.repository.BlogRepository;

import com.moxi.hyblog.search.pojo.ESBlogIndex;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author hzh
 * @since 2020-08-07
 */
@Slf4j
@Service
public class ElasticSearchService {


    @Autowired
    ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    HighlightResultHelper highlightResultHelper;

    @Autowired
    BlogRepository blogRepository;

    public ESBlogIndex buidBlog(Blog eblog) {

        //搜索字段
        String all = eblog.getTitle() + " " + eblog.getSummary() + " " + eblog.getContent();

        //构建blog对象
        ESBlogIndex blog = new ESBlogIndex();
        blog.setId(eblog.getUid());
        blog.setUid(eblog.getUid());
        blog.setTitle(eblog.getTitle());
        blog.setSummary(eblog.getSummary());
        blog.setAll(all);

        if (eblog.getBlogSort() != null) {
            blog.setBlogSort(eblog.getBlogSort());
        }

        //标签列表
        if (eblog.getTagList() != null) {
            blog.setTagList(eblog.getTagList());
        }

        blog.setIsPublish(eblog.getIsPublish());
        blog.setAuthor(eblog.getAuthor());
        blog.setCreateTime(eblog.getCreateTime());
        //封面图片
        if (eblog.getUrl()!=null) {
            blog.setUrl(eblog.getUrl());
        } else {
            blog.setUrl("");
        }

        return blog;
    }

    public Map<String, Object> search(String keywords, Integer currentPage, Integer pageSize) {
        currentPage = Math.max(currentPage - 1, 0);

        //1.高亮查询构造器,需要指定字段和样式
        List<HighlightBuilder.Field> highlightFields = new ArrayList<>();

        HighlightBuilder.Field allField=new HighlightBuilder.Field(SysConf.ALL).preTags("<span style='color:#29bf9d'>").postTags("</span>");
        highlightFields.add(allField);

        HighlightBuilder.Field[] highlightFieldsAry = highlightFields.toArray(new HighlightBuilder.Field[highlightFields.size()]);

        //2.创建查询构造器(总的查询器)
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //2.1分页
        queryBuilder.withPageable(PageRequest.of(currentPage, pageSize));


        //2.2字符串匹配查询
        QueryStringQueryBuilder queryStrBuilder = new QueryStringQueryBuilder(keywords);
        queryStrBuilder.field(SysConf.ALL);

        //2.3往构造器里添加
        queryBuilder.withQuery(queryStrBuilder);

        queryBuilder.withHighlightFields(highlightFieldsAry);

        log.error("查询语句：{}", queryBuilder.build().getQuery().toString());

        //查询highlightResultHelper实现 SearchResultMapper,其中mapResults方法需要对查询到的字段进行手动处理(手动赋值)
       AggregatedPage<ESBlogIndex> result = elasticsearchTemplate.queryForPage(queryBuilder.build(), ESBlogIndex.class, highlightResultHelper);

        //解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<ESBlogIndex> blogList = result.getContent();
        Map<String, Object> map = new HashMap<>();
        map.put(SysConf.TOTAL, total);
        map.put(SysConf.TOTAL_PAGE, totalPage);
        map.put(SysConf.PAGE_SIZE, pageSize);
        map.put(SysConf.CURRENT_PAGE, currentPage + 1);
        map.put(SysConf.BLOG_LIST, blogList);
        return map;
    }

}
