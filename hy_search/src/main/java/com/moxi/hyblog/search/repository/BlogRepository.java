package com.moxi.hyblog.search.repository;

import com.moxi.hyblog.search.pojo.ESBlogIndex;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * BlogRepository操作类
 * 在ElasticsearchRepository中我们可以使用Not Add Like Or Between等关键词自动创建查询语句
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Mapper
public interface BlogRepository extends ElasticsearchRepository<ESBlogIndex, String> {
}
