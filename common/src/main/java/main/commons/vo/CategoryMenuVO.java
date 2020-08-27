package main.commons.vo;
import lombok.Data;
import main.base.validator.annotion.IntegerNotNull;
import main.base.validator.annotion.NotBlank;
import main.base.validator.group.Insert;
import main.base.vo.BaseVO;
import org.apache.ibatis.annotations.Update;


/**
 * <p>
 * 菜单表VO
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月23日10:35:03
 */
@Data
public class CategoryMenuVO extends BaseVO<CategoryMenuVO> {

    /**
     * 菜单名称
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String name;

    /**
     * 菜单级别 （一级分类，二级分类）
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer menuLevel;

    /**
     * 菜单类型 （菜单，按钮）
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer menuType;

    /**
     * 介绍
     */
    private String summary;

    /**
     * Icon图标
     */
    private String icon;

    /**
     * 父UID
     */
    private String parentUid;

    /**
     * URL地址
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String url;

    /**
     * 排序字段(越大越靠前)
     */
    private Integer sort;

    /**
     * 是否显示  1: 是  0: 否
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer isShow;

    /**
     * 是否跳转外部URL，如果是，那么路由为外部的链接
     */
    @IntegerNotNull(groups = {Insert.class, Update.class})
    private Integer isJumpExternalUrl;

}
