package com.moxi.hyblog.sms.feign;

import com.moxi.hyblog.commons.entity.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/24 18:23
 */
@FeignClient(value = "hy-web")
@Service
public interface WebFeignClient {

    @GetMapping("/articlePage/getBlogUIds")
    public List<Blog> getBlogUIds();


}
