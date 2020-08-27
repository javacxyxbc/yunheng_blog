package com.moxi.hyblog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.hyblog.commons.entity.PictureSort;
import com.moxi.hyblog.xo.vo.PictureSortVO;
import com.moxi.hyblog.base.service.SuperService;

import java.util.ArrayList;

/**
 * <p>
 * 图片分类表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
public interface PictureSortService extends SuperService<PictureSort> {

    /**
     * 获取图片分类列表,nginx
     * @param
     * @return
     */
    public IPage<PictureSort> getPageListN(PictureSortVO pictureSortVO);
    /**
     * 获取图片分类列表,七牛云
     *
     * @param pictureSortVO
     * @return
     */
    public IPage<PictureSort> getPageList(PictureSortVO pictureSortVO);

    /**
     * 新增图片分类
     *
     * @param pictureSortVO
     */
    public String addPictureSort(PictureSortVO pictureSortVO);

    /**
     * 编辑图片分类
     *
     * @param pictureSortVO
     */
    public String editPictureSort(PictureSortVO pictureSortVO);

    /**
     * 删除图片分类
     *
     * @param pictureSortVO
     */
    public String deletePictureSort(PictureSortVO pictureSortVO);

    /**
     * 置顶图片分类
     *
     * @param pictureSortVO
     */
    public String stickPictureSort(PictureSortVO pictureSortVO);
}
