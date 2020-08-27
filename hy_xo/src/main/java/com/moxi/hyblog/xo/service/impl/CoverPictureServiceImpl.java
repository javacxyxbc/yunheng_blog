package com.moxi.hyblog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.serviceImpl.SuperServiceImpl;
import com.moxi.hyblog.commons.entity.Comment;
import com.moxi.hyblog.commons.entity.CoverPicture;
import com.moxi.hyblog.commons.entity.Picture;
import com.moxi.hyblog.commons.entity.PictureSort;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.ServerInfo.Sys;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SQLConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.mapper.CommentMapper;
import com.moxi.hyblog.xo.mapper.CoverPictureMapper;
import com.moxi.hyblog.xo.service.CommentService;
import com.moxi.hyblog.xo.service.CoverPictureServer;
import com.moxi.hyblog.xo.service.PictureSortService;
import com.moxi.hyblog.xo.vo.CoverPictureVO;
import com.moxi.hyblog.xo.vo.PictureVO;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/17 23:01
 */
@Service
public class CoverPictureServiceImpl extends SuperServiceImpl<CoverPictureMapper, CoverPicture> implements CoverPictureServer {
    @Autowired
    CoverPictureServer pictureServer;
    @Resource
    CoverPictureMapper coverPictureMapper;
    @Autowired
    PictureSortService pictureSortService;

    @Value("${file.reflect.path}")
    private String reflectPath;
    @Value("${file.upload.path}")
    private String TargetPath;

    @Override
    public IPage<CoverPicture> getPageList(CoverPictureVO pictureVO) {
        //查询构造器
        QueryWrapper<CoverPicture> wrapper=new QueryWrapper<>();
        wrapper.eq(SysConf.SORT_UID,pictureVO.getSortUid());
        wrapper.eq(SQLConf.STATUS,EStatus.ENABLE);
        wrapper.orderByDesc(SQLConf.CREATE_TIME);
        //分页查询
        Page<CoverPicture> page=new Page<>();
        page.setCurrent(pictureVO.getCurrentPage());
        page.setSize(pictureVO.getPageSize());
        IPage<CoverPicture> pageList=pictureServer.page(page,wrapper);
        List<CoverPicture> list=pageList.getRecords();
        for(CoverPicture picture:list){
            picture.setUrl(reflectPath+picture.getUrl());
        }
        pageList.setRecords(list);
        return pageList;
    }

    public String addPictures(MultipartFile[]fileList, String sortUid) throws IOException {
        List<CoverPicture> list=new ArrayList<>();
        for(int i=0;i<fileList.length;i++){
            CoverPicture picture=new CoverPicture();
            picture.setSortUid(sortUid);
            MultipartFile file=fileList[i];
            String url=System.currentTimeMillis()+SysConf.FILE_IMG;
            System.out.println(url);
            String path=TargetPath+SysConf.FILE_SLASH+url;
            file.transferTo(new File(path));
            picture.setUrl(url);
            picture.setStatus(EStatus.ENABLE);
            list.add(picture);
        }
        pictureServer.saveBatch(list);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    public String deleteBatchPicture(CoverPictureVO pictureVO){
        List<String> uids= Arrays.asList(pictureVO.getUid().split(SysConf.FILE_SEGMENTATION));
        coverPictureMapper.deleteBatchIds(uids);
        return ResultUtil.result(SysConf.SUCCESS,MessageConf.DELETE_SUCCESS);
    }

    public String setPictureCover(CoverPictureVO pictureVO){
        PictureSort pictureSort=pictureSortService.getById(pictureVO.getSortUid());
        if(pictureSort!=null){
            CoverPicture picture=pictureServer.getById(pictureVO.getUid());
            if(picture!=null){
                pictureSort.setFileUid(picture.getUid());
                pictureSort.setUpdateTime(new Date());
                pictureSort.updateById();
            }else{
                return ResultUtil.result(SysConf.ERROR,MessageConf.The_PICTURE_DOES_NOT_EXIST);
            }
        }else{
            return ResultUtil.result(SysConf.ERROR,MessageConf.The_PICTURE_SORT_DOES_NOT_EXIST);

        }
        return ResultUtil.result(SysConf.SUCCESS,MessageConf.UPDATE_SUCCESS);
    }

}
