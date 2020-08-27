package com.moxi.hyblog.base.vo;

import com.moxi.hyblog.base.validator.Messages;
import com.moxi.hyblog.base.validator.annotion.LongNotNull;
import com.moxi.hyblog.base.validator.group.GetList;
import lombok.Data;

/**
 * PageVO  用于分页
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Data
public class PageInfo<T> {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 当前页
     */
    @LongNotNull(groups = {GetList.class}, message = Messages.PAGE_NOT_NULL)
    private Long currentPage;

    /**
     * 页大小
     */
    @LongNotNull(groups = {GetList.class}, message = Messages.SIZE_NOT_NULL)
    private Long pageSize;
}
