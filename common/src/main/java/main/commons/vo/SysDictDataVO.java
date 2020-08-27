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
public class SysDictDataVO extends BaseVO<SysDictDataVO> {


    /**
     * 自增键 oid
     */
    private Long oid;

    /**
     * 字典标签
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String dictLabel;

    /**
     * 字典键值
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String dictValue;

    /**
     * 字典类型UID
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String dictTypeUid;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（1是 0否）,默认为0
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer isDefault;

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
