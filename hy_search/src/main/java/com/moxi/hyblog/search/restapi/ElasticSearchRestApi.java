package com.moxi.hyblog.search.restapi;

import com.moxi.hyblog.commons.entity.Blog;
import com.moxi.hyblog.commons.feign.WebFeignClient;
import com.moxi.hyblog.search.global.MessageConf;
import com.moxi.hyblog.search.global.SysConf;
import com.moxi.hyblog.search.repository.BlogRepository;

import com.moxi.hyblog.search.pojo.ESBlogIndex;
import com.moxi.hyblog.search.service.ElasticSearchService;
import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ElasticSearch RestAPI
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@RequestMapping("/search")
@Api(value = "ElasticSearch相关接口", tags = {"ElasticSearch相关接口"})
@RestController
public class ElasticSearchRestApi {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;
    @Autowired
    private ElasticSearchService searchService;
    @Autowired
    private BlogRepository blogRepository;
    @Resource
    private WebFeignClient webFeignClient;




    @ApiOperation(value = "通过ElasticSearch搜索博客", notes = "通过ElasticSearch搜索博客", response = String.class)
    @GetMapping("/elasticSearchBlog")
    public String searchBlog(HttpServletRequest request,
                             @RequestParam("keyWord") String keywords,
                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "30") Integer pageSize) {

        if (StringUtils.isEmpty(keywords)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.KEYWORD_IS_NOT_EMPTY);
        }
        return ResultUtil.result(SysConf.SUCCESS, searchService.search(keywords, currentPage, pageSize));
    }

    @ApiOperation(value = "通过uids删除ElasticSearch博客索引", notes = "通过uids删除ElasticSearch博客索引", response = String.class)
    @PostMapping("/deleteElasticSearchByUids")
    public String deleteElasticSearchByUids(@RequestParam(required = true) String uids) {

        List<String> uidList = StringUtils.changeStringToString(uids, SysConf.FILE_SEGMENTATION);

        for (String uid : uidList) {
            blogRepository.deleteById(uid);
        }

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @ApiOperation(value = "通过博客uid删除ElasticSearch博客索引", notes = "通过uid删除博客", response = String.class)
    @PostMapping("/deleteElasticSearchByUid")
    public String deleteElasticSearchByUid(@RequestParam(required = true) String uid) {
        blogRepository.deleteById(uid);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @ApiOperation(value = "ElasticSearch通过博客Uid添加索引", notes = "添加博客", response = String.class)
    @PostMapping("/addElasticSearchIndexByUid")
    public String addElasticSearchIndexByUid(@RequestParam(required = true) String uid) {

        //通过feign获取文章相关信息
        String result = webFeignClient.FgetByUid(uid);

        Blog eblog = WebUtils.getData(result, Blog.class);
        if (eblog == null) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.INSERT_FAIL);
        }
        //构建存储内容
        ESBlogIndex blog = searchService.buidBlog(eblog);
        blogRepository.save(blog);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.INSERT_SUCCESS);
    }

    @ApiOperation(value = "ElasticSearch初始化索引", notes = "ElasticSearch初始化索引", response = String.class)
    @PostMapping("/initElasticSearchIndex")
    public String initElasticSearchIndex() throws ParseException {
        restTemplate.deleteIndex(ESBlogIndex.class);
        restTemplate.createIndex(ESBlogIndex.class);
        restTemplate.putMapping(ESBlogIndex.class);

        System.out.println("begin-----------");
        Long page = 1L;
        Long row = 10L;
        Integer size = 0;

        do {
            
            // 查询blog信息
            String result = webFeignClient.getNewBlog(page, row);

            if(StringUtils.isBlank(result))break;

            //构建blog
            List<Blog> blogList = WebUtils.getList(result, Blog.class);
            size = blogList.size();

            List<ESBlogIndex> eSBlogIndexList = blogList.stream()
                    .map(searchService::buidBlog).collect(Collectors.toList());
            //存入索引库
            blogRepository.saveAll(eSBlogIndexList);
            // 翻页
            page++;
        } while (size == 15);
        System.out.println("end-------------");
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }


    /**
     * 以下为测试内容
     * @return
     */

    @ApiOperation(value = "获取所有es博客", notes = "获取所有es博客", response = String.class)
    @GetMapping("/getList")
    public Iterable<ESBlogIndex> getList(){
        return blogRepository.findAll();
    }

    @ApiOperation(value = "根据id获取博客", notes = "根据id获取博客", response = String.class)
    @GetMapping("/getById")
    public Optional<ESBlogIndex> getById(@RequestParam("uid")String id){
        return blogRepository.findById(id);
    }

}
