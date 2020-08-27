package main.base.vo;

import lombok.Data;
import main.base.validator.annotion.IdValid;
import main.base.validator.group.Delete;
import main.base.validator.group.Update;

/**
 * BaseVO   view object 表现层 基类对象
 *
 * @author: 陌溪
 * @create: 2019-12-03-22:38
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
