package com.moxi.hyblog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.hyblog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.hyblog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.moxi.hyblog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.hyblog.admin.global.SQLConf;
import com.moxi.hyblog.admin.global.SysConf;
import com.moxi.hyblog.base.enums.EPublish;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.xo.service.BlogService;
import com.moxi.hyblog.xo.vo.BlogVO;
import com.moxi.hyblog.base.exception.ThrowableUtils;
import com.moxi.hyblog.base.validator.group.Delete;
import com.moxi.hyblog.base.validator.group.GetList;
import com.moxi.hyblog.base.validator.group.Insert;
import com.moxi.hyblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 博客表 RestApi
 * </p>
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/blog")
@Api(value = "博客相关接口", tags = {"博客相关接口"})
@Slf4j
public class BlogRestApi {

    //ok
    @Autowired
    BlogService blogService;

    @AuthorityVerify
    @ApiOperation(value = "获取博客列表", notes = "获取博客列表", response = String.class)
    @PostMapping("/getList")
    //AuthorityVerify代表该请求需要验证身份权限,Validated代表需要对blogvo中有getlist.class的参数进行效应
    //BindingResult是和Validated绑在一起的,用于查看数据格式是否正确
    public String getList(@Validated({GetList.class}) @RequestBody BlogVO blogVO, BindingResult result) {
        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, blogService.getPageList1(blogVO));
    }

    //ok
    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加博客")
    @ApiOperation(value = "增加博客", notes = "增加博客", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return blogService.addBlog(blogVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "本地博客上传")
    @ApiOperation(value = "本地博客上传", notes = "本地博客上传", response = String.class)
    @PostMapping("/uploadLocalBlog")
    public String uploadPics(@RequestBody List<MultipartFile> filedatas) throws IOException {

        return blogService.uploadLocalBlog(filedatas);
    }


    @AuthorityVerify
    @OperationLogger(value = "推荐博客排序调整")
    @ApiOperation(value = "推荐博客排序调整", notes = "推荐博客排序调整", response = String.class)
    @PostMapping("/editBatch")
    public String editBatch(@RequestBody List<BlogVO> blogVOList) {
        return blogService.editBatch(blogVOList);
    }

    //ok
    @AuthorityVerify
    @OperationLogger(value = "编辑博客")
    @ApiOperation(value = "编辑博客", notes = "编辑博客", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody BlogVO blogVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return blogService.editBlog(blogVO);
    }


    //ok
    @AuthorityVerify
    @OperationLogger(value = "删除博客")
    @ApiOperation(value = "删除博客", notes = "删除博客", response = String.class)
    @PostMapping("/delete")
    public String delete(@Validated({Delete.class}) @RequestBody BlogVO blogVO, BindingResult result) {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return blogService.deleteBlog(blogVO);
    }

    //ok
    @AuthorityVerify
    @OperationLogger(value = "删除选中博客")
    @ApiOperation(value = "删除选中博客", notes = "删除选中博客", response = String.class)
    @PostMapping("/deleteBatch")
    public String deleteBatch(@RequestBody List<BlogVO> blogVoList) {
        return blogService.deleteBatchBlog(blogVoList);
    }


    @OperationLogger(value = "根据博客uid获取博客内容")
    @ApiOperation(value = "根据博客uid获取博客内容", notes = "根据博客uid获取博客内容", response = String.class)
    @PostMapping("/getBlogContent")
    public String getBlogContent(@RequestBody BlogVO blogVO ) {
            return blogService.getBlogContent(blogVO.getUid());
    }



}