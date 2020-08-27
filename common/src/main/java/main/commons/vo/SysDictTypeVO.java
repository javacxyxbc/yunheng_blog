package main.commons.vo;

import lombok.Data;
import main.base.validator.annotion.IntegerNotNull;
import main.base.validator.annotion.NotBlank;
import main.base.validator.group.Insert;
import main.base.vo.BaseVO;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * SysDictTypeVO
 * </p>
 *
 * @author 陌溪
 * @since 2020年2月15日21:20:03
 */
@Data
public class SysDictTypeVO extends BaseVO<SysDictTypeVO> {


    /**
     * 自增键 oid
     */
    private Long oid;

    /**
     * 字典名称
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String dictName;

    /**
     * 字典类型
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String dictType;

    /**
     * 是否发布  1：是，0:否，默认为0
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String isPublish;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序字段
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer sort;

}
