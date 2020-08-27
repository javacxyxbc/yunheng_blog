package com.moxi.hyblog.admin.restapi;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.hyblog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.hyblog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.hyblog.admin.global.SysConf;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.commons.entity.CoverPicture;
import com.moxi.hyblog.commons.feign.UploadFeignClient;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.xo.service.CoverPictureServer;
import com.moxi.hyblog.xo.service.PictureService;
import com.moxi.hyblog.xo.vo.CoverPictureVO;
import com.moxi.hyblog.xo.vo.PictureVO;
import com.moxi.hyblog.base.exception.ThrowableUtils;
import com.moxi.hyblog.base.validator.group.GetList;
import com.moxi.hyblog.base.validator.group.Insert;
import com.moxi.hyblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 图片表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月17日16:21:43
 */
@RestController
@RequestMapping("/picture")
@Api(value = "图片相关接口", tags = {"图片相关接口"})
@Slf4j
public class PictureRestApi {

    @Autowired
    PictureService pictureService;

    @Autowired
    CoverPictureServer coverPictureServer;

    @Value("${file.upload.path}")
    private String TargetPath;

    @AuthorityVerify
    @ApiOperation(value = "获取图片列表", notes = "获取图片列表", response = String.class)
    @PostMapping(value = "/getList")
    public String getList(@Validated({GetList.class}) @RequestBody CoverPictureVO pictureVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS,coverPictureServer.getPageList(pictureVO));
    }

    @AuthorityVerify
    @OperationLogger(value = "增加图片")
    @ApiOperation(value = "增加图片", notes = "增加图片", response = String.class)
    @PostMapping("/add")
    public String add1(@RequestParam("file")MultipartFile[] fileList,@RequestParam("sortUid")String sortUid) throws IOException {
        return coverPictureServer.addPictures(fileList,sortUid);
    }



    @AuthorityVerify
    @OperationLogger(value = "删除图片")
    @ApiOperation(value = "删除图片", notes = "删除图片", response = String.class)
    @PostMapping("/delete")
    public String delete(@RequestBody CoverPictureVO pictureVO, BindingResult result) {
        return coverPictureServer.deleteBatchPicture(pictureVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "通过图片Uid将图片设为封面")
    @ApiOperation(value = "通过图片Uid将图片设为封面", notes = "通过图片Uid将图片设为封面", response = String.class)
    @PostMapping("/setCover")
    public String setCover( @RequestBody CoverPictureVO pictureVO, BindingResult result) {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return coverPictureServer.setPictureCover(pictureVO);
    }
}

