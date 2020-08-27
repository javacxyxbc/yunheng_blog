package com.moxi.hyblog.web.restapi;


import com.moxi.hyblog.base.enums.EBehavior;
import com.moxi.hyblog.web.log.BussinessLog;
import com.moxi.hyblog.xo.service.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 归档 RestApi
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/sort")
@Api(value = "博客归档相关接口", tags = {"博客归档相关接口"})
@Slf4j
public class SortRestApi {

    @Autowired
    BlogService blogService;
    @ApiOperation(value = "归档", notes = "归档")
    @GetMapping("/getSortListY")
    public String getSortListY() {
        log.info("获取归档年份");
        return blogService.getBlogTimeSortListY();
    }

    @ApiOperation(value = "归档", notes = "归档")
    @GetMapping("/getSortList")
    public String getSortList() {
        log.info("获取归档日期");
        return blogService.getBlogTimeSortList();
    }

    @BussinessLog(value = "点击归档", behavior = EBehavior.VISIT_SORT)
    @ApiOperation(value = "通过月份获取文章", notes = "通过月份获取文章")
    @GetMapping("/getArticleByMonth")
    public String getArticleByMonth(@ApiParam(name = "monthDate", value = "归档的日期", required = false) @RequestParam(name = "monthDate", required = false) String monthDate) {
        log.info("通过月份获取文章列表");
        return blogService.getArticleByMonth(monthDate);
    }
}

