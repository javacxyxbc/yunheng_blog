package com.moxi.hyblog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.commons.entity.BlogSort;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SQLConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.vo.BlogSortVO;
import com.moxi.hyblog.xo.mapper.BlogSortMapper;
import com.moxi.hyblog.xo.service.BlogService;
import com.moxi.hyblog.xo.service.BlogSortService;
import com.moxi.hyblog.base.enums.EPublish;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class BlogSortServiceImpl extends SuperServiceImpl<BlogSortMapper, BlogSort> implements BlogSortService {

    @Autowired
    BlogSortService blogSortService;

    @Autowired
    BlogService blogService;
    @Resource
    BlogSortMapper blogSortMapper;
    @Override
    public IPage<BlogSort> getPageList(BlogSortVO blogSortVO) {

        QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
        //如果存在关键字,就增加like查询
        if (StringUtils.isNotEmpty(blogSortVO.getKeyword()) && !StringUtils.isEmpty(blogSortVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.SORT_NAME, blogSortVO.getKeyword().trim());
        }
        Page<BlogSort> page = new Page<>();
        page.setCurrent(blogSortVO.getCurrentPage());
        page.setSize(blogSortVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        //根据sort字段降序排序
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<BlogSort> pageList = blogSortService.page(page, queryWrapper);
        return pageList;
    }

    public IPage<BlogSort> getSortNameWithCnt(){
        List<BlogSort> list=blogSortMapper.getSortNameWithCnt();
        IPage<BlogSort> page=new Page<>();
        page.setRecords(list);
        page.setTotal(list.size());
        return page;
    }
    @Override
    public List<BlogSort> getList() {
        QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        List<BlogSort> blogSortList = blogSortService.list(queryWrapper);
        return blogSortList;
    }

    @Override
    public String addBlogSort(BlogSortVO blogSortVO) {
        // 判断添加的分类是否存在
        //首先根据名字去查询
        QueryWrapper<BlogSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, blogSortVO.getSortName());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        int count=blogSortService.count(queryWrapper);
        if (count >0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.ENTITY_EXIST);
        }
        //不存在就封装数据,并直接调用insert
        BlogSort blogSort = new BlogSort();
        blogSort.setContent(blogSortVO.getContent());
        blogSort.setSortName(blogSortVO.getSortName());
        blogSort.setSort(blogSortVO.getSort());
        blogSort.setStatus(EStatus.ENABLE);
        blogSort.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @Override
    //现根据uid查询,
    public String editBlogSort(BlogSortVO blogSortVO) {
        BlogSort blogSort = blogSortService.getById(blogSortVO.getUid());
        if(blogSort==null)return ResultUtil.result(SysConf.ERROR,MessageConf.ENTITY_NOT_EXIST);
        blogSort.setContent(blogSortVO.getContent());
        blogSort.setSortName(blogSortVO.getSortName());
        blogSort.setSort(blogSortVO.getSort());
        blogSort.setStatus(EStatus.ENABLE);
        blogSort.setUpdateTime(new Date());
        blogSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteBatchBlogSort(List<BlogSortVO> blogSortVoList) {
        if (blogSortVoList.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        List<String> uids = new ArrayList<>();

        blogSortVoList.forEach(item -> {
            uids.add(item.getUid());
        });

        // 判断要删除的分类，是否有博客
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        blogQueryWrapper.in(SQLConf.BLOG_SORT_UID, uids);
        Integer blogCount = blogService.count(blogQueryWrapper);
        if (blogCount > 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.BLOG_UNDER_THIS_SORT);
        }

        Collection<BlogSort> blogSortList = blogSortService.listByIds(uids);

        blogSortList.forEach(item -> {
            item.setUpdateTime(new Date());
            item.setStatus(EStatus.DISABLED);
        });

        Boolean save = blogSortService.updateBatchById(blogSortList);

        if (save) {
            return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
        } else {
            return ResultUtil.result(SysConf.ERROR, MessageConf.DELETE_FAIL);
        }
    }

    @Override
    //先查出最大的分类,现在要置顶的sort就是原先最大的sort+1
    public String stickBlogSort(BlogSortVO blogSortVO) {
        BlogSort blogSort = blogSortService.getById(blogSortVO.getUid());

        //查找出最大的那一个
        BlogSort maxSort =blogSortMapper.GetMaxSortCnt();
        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        if (maxSort.getUid().equals(blogSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.THIS_SORT_IS_TOP);
        }

        Integer sortCount = maxSort.getSort() + 1;
        blogSort.setSort(sortCount);
        blogSort.setUpdateTime(new Date());
        blogSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public String blogSortByClickCount() {
        QueryWrapper<BlogSort> queryWrapper = new QueryWrapper();

        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);

        // 按点击从高到低排序
        queryWrapper.orderByDesc(SQLConf.CLICK_COUNT);

        List<BlogSort> blogSortList = blogSortService.list(queryWrapper);

        // 设置初始化最大的sort值
        Integer maxSort = blogSortList.size();
        for (BlogSort item : blogSortList) {
            item.setSort(item.getClickCount());
            item.setUpdateTime(new Date());
        }
        blogSortService.updateBatchById(blogSortList);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public String blogSortByCite() {
        // 定义Map   key：tagUid,  value: 引用量
        Map<String, Integer> map = new HashMap<>();

        QueryWrapper<BlogSort> blogSortQueryWrapper = new QueryWrapper<>();
        blogSortQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<BlogSort> blogSortList = blogSortService.list(blogSortQueryWrapper);
        // 初始化所有标签的引用量
        blogSortList.forEach(item -> {
            map.put(item.getUid(), 0);
        });

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.IS_PUBLISH, EPublish.PUBLISH);
        // 过滤content字段
        queryWrapper.select(Blog.class, i -> !i.getProperty().equals(SQLConf.CONTENT));
        List<Blog> blogList = blogService.list(queryWrapper);

        blogList.forEach(item -> {
            String blogSortUid = item.getBlogSortUid();
            if (map.get(blogSortUid) != null) {
                Integer count = map.get(blogSortUid) + 1;
                map.put(blogSortUid, count);
            } else {
                map.put(blogSortUid, 0);
            }
        });

        blogSortList.forEach(item -> {
            item.setSort(map.get(item.getUid()));
            item.setUpdateTime(new Date());
        });
        blogSortService.updateBatchById(blogSortList);

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @Override
    public BlogSort getTopOne() {
        QueryWrapper<BlogSort> blogSortQueryWrapper = new QueryWrapper<>();
        blogSortQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        blogSortQueryWrapper.last("LIMIT 1");
        blogSortQueryWrapper.orderByDesc(SQLConf.SORT);
        BlogSort blogSort = blogSortService.getOne(blogSortQueryWrapper);
        return blogSort;
    }
}
