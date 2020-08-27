package com.moxi.hyblog.base.vo;

import com.moxi.hyblog.base.validator.annotion.IdValid;
import com.moxi.hyblog.base.validator.group.Delete;
import com.moxi.hyblog.base.validator.group.Update;
import lombok.Data;

/**
 * BaseVO   view object 表现层 基类对象
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Data
public class BaseVO<T> extends PageInfo<T> {

    /**
     * 唯一UID
     */
    @IdValid(groups = {Update.class, Delete.class})
    private String uid;

    private Integer status;
}
