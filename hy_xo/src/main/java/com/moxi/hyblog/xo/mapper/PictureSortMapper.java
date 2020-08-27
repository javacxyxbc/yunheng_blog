package com.moxi.hyblog.xo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.hyblog.commons.entity.PictureSort;
import com.moxi.hyblog.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Wrapper;
import java.util.ArrayList;

/**
 * <p>
 * 图片分类表 Mapper 接口
 * </p>
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
public interface PictureSortMapper extends SuperMapper<PictureSort> {
    @Select("SELECT t1.*,t2.url AS CoverUrl FROM t_picture_sort t1 left JOIN t_coverpicture t2 " +
            "on t1.file_uid=t2.uid " +
            "${ew.customSqlSegment} ")
    //ew.customSqlSegment指wrapper中的条件
    public IPage<PictureSort> getPictureSortList(Page<PictureSort> page,
                                                 @Param(Constants.WRAPPER) QueryWrapper<PictureSort> queryWrapper);
}
