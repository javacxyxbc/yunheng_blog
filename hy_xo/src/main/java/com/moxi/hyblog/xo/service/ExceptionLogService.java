package com.moxi.hyblog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.hyblog.commons.entity.ExceptionLog;
import com.moxi.hyblog.xo.vo.ExceptionLogVO;
import com.moxi.hyblog.base.service.SuperService;

/**
 * <p>
 * 操作异常日志 服务类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
public interface ExceptionLogService extends SuperService<ExceptionLog> {

    /**
     * 获取异常日志列表
     *
     * @param exceptionLogVO
     * @return
     */
    public IPage<ExceptionLog> getPageList(ExceptionLogVO exceptionLogVO);
}
