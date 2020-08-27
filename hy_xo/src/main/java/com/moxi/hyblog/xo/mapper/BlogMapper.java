package com.moxi.hyblog.xo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.base.mapper.SuperMapper;
import com.moxi.hyblog.xo.vo.BlogVO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 Mapper 接口
 * </p>
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
public interface BlogMapper extends SuperMapper<Blog> {



    /**
     * 获取博客列表
     */
    @Select("SELECT t1.uid,t1.title,t1.summary,t1.tag_uid,t1.click_count,t1.file_uid,t1.collect_count,t1.status,t1.create_time,t1.is_original,\n" +
            "t1.author,t1.level,t1.is_publish,t1.sort,t1.start_comment,t1.articles_part,t1.blog_sort_uid,\n" +
            "t2.url FROM \n" +
            "t_blog t1  LEFT JOIN t_coverpicture t2 ON t1.file_uid=t2.uid \n" +
            " ${ew.customSqlSegment}")
    @Results({
            //mybatis一对一选出blogsort,一对多选出tags
            @Result(column ="blog_sort_uid",property ="blogSort",one=@One(select = "com.moxi.hyblog.xo.mapper.BlogSortMapper.getBlogSortById")),
            @Result(column = "tag_uid",property = "tagList",many = @Many(select = "com.moxi.hyblog.xo.mapper.TagMapper.getTagsByUid"))
    })
    public IPage<Blog> getBlogList(Page<Blog> page,@Param(Constants.WRAPPER)QueryWrapper<Blog> queryWrapper);


    /**
     * 获取一篇博客的所有内容
     */
    @Select("SELECT t1.uid,t1.title,t1.summary,t1.content,t1.tag_uid,t1.click_count,t1.prise_count,t1.file_uid,t1.collect_count,t1.status,t1.create_time,t1.is_original,\n" +
            "t1.author,t1.level,t1.is_publish,t1.sort,t1.start_comment,t1.articles_part,t1.blog_sort_uid,\n" +
            "t2.url FROM \n" +
            "t_blog t1  LEFT JOIN t_coverpicture t2 ON t1.file_uid=t2.uid \n" +
            " ${ew.customSqlSegment}")
    @Results({
            //mybatis一对一选出blogsort,一对多选出tags
            @Result(column ="blog_sort_uid",property ="blogSort",one=@One(select = "com.moxi.hyblog.xo.mapper.BlogSortMapper.getBlogSortById")),
            @Result(column = "tag_uid",property = "tagList",many = @Many(select = "com.moxi.hyblog.xo.mapper.TagMapper.getTagsByUid"))
    })
    public Blog getBlog(@Param(Constants.WRAPPER)QueryWrapper<Blog> queryWrapper);
    /**
     * 获取一篇博客所有相关内容,返回map类型
     */
    @Select("SELECT t1.uid,t1.title,t1.summary,t1.content,t1.tag_uid ,t1.click_count as clickCount,t1.prise_count as priseCount,t1.file_uid as fileUid,t1.collect_count as collectCount,t1.status,t1.create_time as createTime,t1.is_original as isOriginal,\n" +
            "t1.author,t1.level,t1.is_publish as isPublish ,t1.sort,t1.start_comment as startComment,t1.articles_part as articlesPart,t1.blog_sort_uid,\n" +
            "t2.url FROM \n" +
            "t_blog t1  LEFT JOIN t_coverpicture t2 ON t1.file_uid=t2.uid \n" +
            " ${ew.customSqlSegment}")
    @Results({
            //mybatis一对一选出blogsort,一对多选出tags
            @Result(column ="blog_sort_uid",property ="blogSort",one=@One(select = "com.moxi.hyblog.xo.mapper.BlogSortMapper.getBlogSortById")),
            @Result(column = "tag_uid",property = "tagList",many = @Many(select = "com.moxi.hyblog.xo.mapper.TagMapper.getTagsByUid"))
    })
    public Map getBlogAsMap(@Param(Constants.WRAPPER)QueryWrapper<Blog> queryWrapper);
    /**
     * 获取博客列表,不包含内容
     */
    @Select("SELECT t1.uid,t1.title,t1.click_count,t1.create_time,t1.is_original,\n" +
            "t1.author,t1.sort,\n" +
            "t2.url,\n" +
            "t3.sort_name as sortName \n" +
            "FROM t_blog t1  LEFT JOIN t_coverpicture t2 ON t1.file_uid=t2.uid\n" +
            "LEFT JOIN t_blog_sort t3 ON t1.blog_sort_uid=t3.uid \n" +
            "${ew.customSqlSegment}")
    public IPage<Blog> getList(Page<Blog>page,@Param(Constants.WRAPPER) QueryWrapper<Blog> queryWrapper);


    /**
     * 搜索相关博客的推荐博客
     */

    /**
     * 删除博客
     */
    @Update("update t_blog set status=0 where uid =#{uid}")
    public int deleteBlog(@Param("uid")String uid);

    /**
     * 批量删除
     */
    @UpdateProvider(type = BlogProvider.class,method = "deleteBatchBlog")
    public int deleteBatchBlog(@Param("list")ArrayList<String>list);

    /**
     *
     */
    /**
     * 通过标签获取博客数量
     *
     * @return
     */
    @Select("SELECT tag_uid, COUNT(tag_uid) as count FROM  t_blog GROUP BY tag_uid")
    List<Map<String, Object>> getBlogCountByTag();

    /**
     * 通过分类获取博客数量
     *
     * @return
     */
    @Select("SELECT blog_sort_uid, COUNT(blog_sort_uid) AS count FROM  t_blog where status = 1 GROUP BY blog_sort_uid")
    List<Map<String, Object>> getBlogCountByBlogSort();

    /**
     * 获取一年内的文章贡献数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("SELECT DISTINCT DATE_FORMAT(create_time, '%Y-%m-%d') DATE, COUNT(uid) COUNT FROM t_blog WHERE 1=1 && status = 1 && create_time >= #{startTime} && create_time < #{endTime} GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d')")
    List<Map<String, Object>> getBlogContributeCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取首页博客,标签,分类数
     */
    @Select("SELECT COUNT(*) AS blogCount FROM t_blog WHERE STATUS=1 AND is_publish=1 \n" +
            "UNION ALL\n" +
            "SELECT COUNT(*) AS tagCount FROM t_tag WHERE STATUS=1\n" +
            "UNION ALL\n" +
            "SELECT COUNT(*) AS sortCount FROM t_blog_sort WHERE STATUS=1;")
    public List<Integer> getCollect();

    /**
     * 批量更新点击量,访问量
     */

    @UpdateProvider(type = BlogProvider.class,method = "updateCollectBatch")
    public int updateCollectBatch(@Param("list") List<Blog> list);


    public class BlogProvider {
        /**
         * 批量删除
         * @param list
         * @return
         */
        public String deleteBatchBlog(@Param("list")ArrayList<String> list){
            StringBuilder sb=new StringBuilder();
            sb.append("update t_blog set status=0 where uid in (");
            for(int i=0;i<list.size();i++){
                sb.append("'"+list.get(i)+"'");
                if(i!=list.size()-1)sb.append(",");
            }
            sb.append(")");
            return sb.toString();
        }
        /**
         * 批量更新
         *
         *UPDATE t_blog SET
         *   click_count = CASE uid
         *      WHEN "03d2c2766581229c68bd386c2de3fb6e" THEN 20
         *      WHEN "5dcfa17cb4d48b85701f8612fc6f47c0" THEN 30
         *      WHEN "7d279e3ee9a643cc06364f4f75fbf3ee" THEN 50
         *   END,
         *   prise_count = CASE uid
         *     WHEN "03d2c2766581229c68bd386c2de3fb6e" THEN 20
         *      WHEN "5dcfa17cb4d48b85701f8612fc6f47c0" THEN 30
         *      WHEN "7d279e3ee9a643cc06364f4f75fbf3ee" THEN 50
         *  END;
         */
        public String updateCollectBatch(@Param("list") List<Blog> list){
            StringBuilder sb=new StringBuilder();
            sb.append("UPDATE t_blog SET \n");
            sb.append("click_count = CASE uid \n");
            for(Blog blog:list){
                sb.append("when '");
                sb.append(blog.getUid()+"' then "+blog.getClickCount() +"\n");
            }
            sb.append("end, \n");
            sb.append("prise_count = CASE uid \n");
            for(Blog blog:list){
                sb.append("when '");
                sb.append(blog.getUid()+"' then "+blog.getPriseCount() +"\n");
            }
            sb.append("end;");
            return sb.toString();
        }
    }

}
