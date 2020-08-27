package com.moxi.hyblog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.hyblog.commons.entity.Picture;
import com.moxi.hyblog.commons.entity.PictureSort;
import com.moxi.hyblog.commons.feign.PictureFeignClient;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SQLConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.utils.WebUtil;
import com.moxi.hyblog.xo.vo.PictureSortVO;
import com.moxi.hyblog.xo.mapper.PictureSortMapper;
import com.moxi.hyblog.xo.service.PictureService;
import com.moxi.hyblog.xo.service.PictureSortService;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.serviceImpl.SuperServiceImpl;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 图片分类表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Service
public class PictureSortServiceImpl extends SuperServiceImpl<PictureSortMapper, PictureSort> implements PictureSortService {

    @Autowired
    WebUtil webUtil;

    @Autowired
    PictureSortService pictureSortService;

    @Autowired
    PictureService pictureService;

    @Autowired
    PictureFeignClient pictureFeignClient;

    @Resource
    PictureSortMapper pictureSortMapper;

    @Value("${file.reflect.path}")
    private String reflectPath;

    //nginx版本
    public IPage<PictureSort> getPageListN(PictureSortVO pictureSortVO){
        Page<PictureSort> page=new Page<>();
        QueryWrapper<PictureSort> wrapper=new QueryWrapper<>();
        if(StringUtils.isNotEmpty(pictureSortVO.getKeyword())){
            wrapper.like(SQLConf.NAME,pictureSortVO.getKeyword().trim());
        }
        wrapper.eq(SQLConf.TABLE_T1_STATUS,EStatus.ENABLE);
        wrapper.orderByDesc(SQLConf.TABLE_T1_SORT);
        page.setCurrent(pictureSortVO.getCurrentPage());
        page.setSize(pictureSortVO.getPageSize());
        IPage<PictureSort> list=pictureSortMapper.getPictureSortList(page,wrapper);
        for(PictureSort sort:list.getRecords()){
            sort.setCoverUrl(reflectPath+sort.getCoverUrl());
        }
       return list;
    }
    //七牛云版本
    @Override
    public IPage<PictureSort> getPageList(PictureSortVO pictureSortVO) {
        QueryWrapper<PictureSort> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(pictureSortVO.getKeyword()) && !StringUtils.isEmpty(pictureSortVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.NAME, pictureSortVO.getKeyword().trim());
        }

        if (pictureSortVO.getIsShow() != null) {
            queryWrapper.eq(SQLConf.IS_SHOW, SysConf.ONE);
        }
        Page<PictureSort> page = new Page<>();
        page.setCurrent(pictureSortVO.getCurrentPage());
        page.setSize(pictureSortVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<PictureSort> pageList = pictureSortService.page(page, queryWrapper);
        List<PictureSort> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
        });
        String pictureResult = null;
        Map<String, String> pictureMap = new HashMap<>();
        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtil.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get(SysConf.UID).toString(), item.get(SysConf.URL).toString());
        });

        for (PictureSort item : list) {
            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();
                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        pageList.setRecords(list);
        return pageList;
    }

    @Override
    public String addPictureSort(PictureSortVO pictureSortVO) {
        PictureSort pictureSort = new PictureSort();
        pictureSort.setName(pictureSortVO.getName());
        pictureSort.setParentUid(pictureSortVO.getParentUid());
        pictureSort.setSort(pictureSortVO.getSort());
        pictureSort.setFileUid(pictureSortVO.getFileUid());
        pictureSort.setStatus(EStatus.ENABLE);
        pictureSort.setIsShow(pictureSortVO.getIsShow());
        pictureSort.setUpdateTime(new Date());
        pictureSort.insert();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editPictureSort(PictureSortVO pictureSortVO) {
        PictureSort pictureSort = pictureSortService.getById(pictureSortVO.getUid());
        pictureSort.setName(pictureSortVO.getName());
        pictureSort.setParentUid(pictureSortVO.getParentUid());
        pictureSort.setSort(pictureSortVO.getSort());
        pictureSort.setFileUid(pictureSortVO.getFileUid());
        pictureSort.setIsShow(pictureSortVO.getIsShow());
        pictureSort.setUpdateTime(new Date());
        pictureSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deletePictureSort(PictureSortVO pictureSortVO) {
        // 判断要删除的分类，是否有图片
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        pictureQueryWrapper.eq(SQLConf.PICTURE_SORT_UID, pictureSortVO.getUid());
        Integer pictureCount = pictureService.count(pictureQueryWrapper);
        if (pictureCount > 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PICTURE_UNDER_THIS_SORT);
        }

        PictureSort pictureSort = pictureSortService.getById(pictureSortVO.getUid());
        pictureSort.setStatus(EStatus.DISABLED);
        pictureSort.setUpdateTime(new Date());
        pictureSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String stickPictureSort(PictureSortVO pictureSortVO) {
        PictureSort pictureSort = pictureSortService.getById(pictureSortVO.getUid());
        //查找出最大的那一个
        QueryWrapper<PictureSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SQLConf.SORT);
        Page<PictureSort> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<PictureSort> pageList = pictureSortService.page(page, queryWrapper);
        List<PictureSort> list = pageList.getRecords();
        PictureSort maxSort = list.get(0);
        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PARAM_INCORRECT);
        }
        if (maxSort.getUid().equals(pictureSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.THIS_SORT_IS_TOP);
        }
        Integer sortCount = maxSort.getSort() + 1;
        pictureSort.setSort(sortCount);
        pictureSort.setUpdateTime(new Date());
        pictureSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}
