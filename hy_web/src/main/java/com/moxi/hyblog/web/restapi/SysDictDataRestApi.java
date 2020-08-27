package com.moxi.hyblog.web.restapi;


import com.moxi.hyblog.utils.ResultUtil;
import com.moxi.hyblog.web.global.SysConf;
import com.moxi.hyblog.xo.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典数据查询 RestApi
 * </p>
 *
 * @author 陌溪
 * @since 2020年3月11日10:37:13
 */
@RestController
@RequestMapping("/sysDictData")
@Api(value = "数据字典相关接口", tags = {"数据字典相关接口"})
@Slf4j
public class SysDictDataRestApi {

    @Autowired
    SysDictDataService sysDictDataService;

    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = String.class)
    @PostMapping("/getListByDictType")
    public String getListByDictType(@RequestParam("dictType") String dictType) {

        log.info("根据字典类型获取字典数据");
        return ResultUtil.result(SysConf.SUCCESS, sysDictDataService.getListByDictType(dictType));
    }

    @ApiOperation(value = "根据字典类型数组获取字典数据", notes = "根据字典类型数组获取字典数据", response = String.class)
    @PostMapping("/getListByDictTypeList")
    public String getListByDictTypeList(@RequestBody List<String> dictTypeList) {
        log.info("根据字典类型数组获取字典数据");
        return ResultUtil.result(SysConf.SUCCESS, sysDictDataService.getListByDictTypeList(dictTypeList));
    }

}

