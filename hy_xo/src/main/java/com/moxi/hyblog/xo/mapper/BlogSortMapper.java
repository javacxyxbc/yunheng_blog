package com.moxi.hyblog.xo.mapper;

import com.moxi.hyblog.commons.entity.BlogSort;
import com.moxi.hyblog.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * <p>
 * 博客分类表 Mapper 接口
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年9月24日15:15:44
 */

public interface BlogSortMapper extends SuperMapper<BlogSort> {
    /**
     * 选出uid,maxSort
     */
    @Select("select uid,max(sort) from t_blog_sort")
    public BlogSort GetMaxSortCnt();

    @Select("select uid,sort_name,content from t_blog_sort where uid=#{uid}")
    public BlogSort getBlogSortById(@Param("blog_sort_uid")String uid);

    /**
     * 获取分类名及各个分类下的博客数量
     */
    @Select("SELECT t1.uid AS uid,t1.sort_name AS sortName," +
            "t2.cnt AS cnt " +
            "FROM t_blog_sort t1 LEFT JOIN \n" +
            "(SELECT blog_sort_uid,COUNT(*) AS cnt FROM t_blog  GROUP BY blog_sort_uid) " +
            "t2 \n" +
            "ON t1.uid=t2.blog_sort_uid \n" +
            "WHERE t1.status=1")
    public List<BlogSort> getSortNameWithCnt();
}
