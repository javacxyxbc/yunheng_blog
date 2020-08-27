package com.moxi.hyblog.commons.feign;

import com.moxi.hyblog.commons.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/19 15:20
 */
@Service
@FeignClient(value = "upload-service",configuration = FeignConfiguration.class)
public interface UploadFeignClient {
    /**
     * 传输图片
     */
    @PostMapping(value = "/upload/uploadPicture")
    public String uploadArticle(@RequestParam("file") MultipartFile file) ;
}
