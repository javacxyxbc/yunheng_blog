package com.moxi.hyblog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.hyblog.base.service.SuperService;
import com.moxi.hyblog.commons.entity.CoverPicture;
import com.moxi.hyblog.commons.entity.Picture;
import com.moxi.hyblog.xo.vo.CoverPictureVO;
import com.moxi.hyblog.xo.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/17 23:01
 */
public interface CoverPictureServer extends SuperService<CoverPicture> {
    /**
     * 获取图片列表
     *
     * @param pictureVO
     * @return
     */
    public IPage<CoverPicture> getPageList(CoverPictureVO pictureVO);

    public String addPictures(MultipartFile []fileList,String sortUid) throws IOException;
//    /**
//     * 编辑图片
//     *
//     * @param pictureVO
//     */
//    public String editPicture(PictureVO pictureVO);
//
    /**
     * 批量删除图片
     *
     * @param pictureVO
     */
    public String deleteBatchPicture(CoverPictureVO pictureVO);

    /**
     * 设置图片封面
     *
     * @param pictureVO
     */
    public String setPictureCover(CoverPictureVO pictureVO);
//
//    /**
//     * 获取最新图片,按时间排序
//     *
//     * @return
//     */
//    public Picture getTopOne();
}
