//package com.moxi.hyblog.web.restapi;
//
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.moxi.hyblog.base.enums.EBehavior;
//import com.moxi.hyblog.base.enums.EPublish;
//import com.moxi.hyblog.base.enums.EStatus;
//import com.moxi.hyblog.base.global.ECode;
//import com.moxi.hyblog.base.holder.RequestHolder;
//import com.moxi.hyblog.commons.entity.Blog;
//import com.moxi.hyblog.commons.feign.PictureFeignClient;
//import com.moxi.hyblog.utils.IpUtils;
//import com.moxi.hyblog.utils.ResultUtil;
//import com.moxi.hyblog.utils.StringUtils;
//import com.moxi.hyblog.web.global.SysConf;
//
//import com.moxi.hyblog.web.log.BussinessLog;
//import com.moxi.hyblog.xo.global.MessageConf;
//import com.moxi.hyblog.xo.service.*;
//import com.moxi.hyblog.xo.utils.WebUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * <p>
// * 文章详情 RestApi
// * </p>
// *
// * @author xuzhixiang
// * @since 2018-09-04
// */
//@RestController
//@RequestMapping("/content")
//@Api(value = "文章详情相关接口", tags = {"文章详情相关接口"})
//@Slf4j
//public class BlogContentRestApi {
//
//    @Autowired
//    WebUtil webUtil;
//
//    @Autowired
//    TagService tagService;
//
//    @Autowired
//    BlogSortService blogSortService;
//
//    @Autowired
//    LinkService linkService;
//    @Autowired
//    CommentService commentService;
//    @Autowired
//    private BlogService blogService;
//    @Autowired
//    private PictureFeignClient pictureFeignClient;
//    @Autowired
//    private WebVisitService webVisitService;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Value(value = "${BLOG.ORIGINAL_TEMPLATE}")
//    private String ORIGINAL_TEMPLATE;
//
//    @Value(value = "${BLOG.REPRINTED_TEMPLATE}")
//    private String REPRINTED_TEMPLATE;
//
//
//    @BussinessLog(value = "点击博客", behavior = EBehavior.BLOG_CONTNET)
//    @ApiOperation(value = "通过Uid获取博客内容", notes = "通过Uid获取博客内容")
//    @GetMapping("/getBlogByUid")
//    public String getBlogByUid(@ApiParam(name = "uid", value = "博客UID", required = false) @RequestParam(name = "uid", required = false) String uid) {
//
////        HttpServletRequest request = RequestHolder.getRequest();
////        String ip = IpUtils.getIpAddr(request);
////
//        if (StringUtils.isEmpty(uid)) {
//            return ResultUtil.result(SysConf.ERROR, "UID不能为空");
//        }
//
//
//        return ResultUtil.result(SysConf.SUCCESS, blogService.getBlogById(uid));
//    }
//
//
//
//    @ApiOperation(value = "根据标签Uid获取相关的博客", notes = "根据标签获取相关的博客")
//    @GetMapping("/getSameBlogByTagUid")
//    public String getSameBlogByTagUid(@ApiParam(name = "tagUid", value = "博客标签UID", required = true) @RequestParam(name = "tagUid", required = true) String tagUid,
//                                      @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
//                                      @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {
//        if (StringUtils.isEmpty(tagUid)) {
//            return ResultUtil.result(SysConf.ERROR, "标签UID不能为空");
//        }
//        return ResultUtil.result(SysConf.SUCCESS, blogService.getSameBlogByTagUid(tagUid));
//    }
//
//    @ApiOperation(value = "根据BlogUid获取相关的博客", notes = "根据BlogUid获取相关的博客")
//    @GetMapping("/getSameBlogByBlogUid")
//    public String getSameBlogByBlogUid(HttpServletRequest request,
//                                       @ApiParam(name = "blogUid", value = "博客标签UID", required = true) @RequestParam(name = "blogUid", required = true) String blogUid) {
//        if (StringUtils.isEmpty(blogUid)) {
//            return ResultUtil.result(SysConf.ERROR, "博客UID不能为空");
//        }
//        List<Blog> blogList = blogService.getSameBlogByBlogUid(blogUid);
//        IPage<Blog> pageList = new Page<>();
//        pageList.setRecords(blogList);
//        return ResultUtil.result(SysConf.SUCCESS, pageList);
//    }
//
//    /**
//     * 设置博客标题图
//     *
//     * @param blog
//     */
//    private void setPhotoListByBlog(Blog blog) {
//        //获取标题图片
//        if (blog != null && !StringUtils.isEmpty(blog.getFileUid())) {
//            String result = this.pictureFeignClient.getPicture(blog.getFileUid(), ",");
//            List<String> picList = webUtil.getPicture(result);
//            log.info("##### picList: #######" + picList);
//            if (picList != null && picList.size() > 0) {
//                blog.setPhotoList(picList);
//            }
//        }
//    }
//
//    /**
//     * 设置博客版权
//     *
//     * @param blog
//     */
//    private void setBlogCopyright(Blog blog) {
//
//        //如果是原创的话
//        if (blog.getIsOriginal().equals("1")) {
//            blog.setCopyright(ORIGINAL_TEMPLATE);
//        } else {
//            String reprintedTemplate = REPRINTED_TEMPLATE;
//            String[] variable = {blog.getArticlesPart(), blog.getAuthor()};
//            String str = String.format(reprintedTemplate, variable);
//            blog.setCopyright(str);
//        }
//    }
//}
//
