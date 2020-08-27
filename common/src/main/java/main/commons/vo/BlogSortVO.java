package main.commons.vo;

import lombok.Data;
import main.base.validator.annotion.NotBlank;
import main.base.validator.group.Insert;
import main.base.vo.BaseVO;
import org.apache.ibatis.annotations.Update;

/**
 * BlogSortVO
 *
 * @author: 陌溪
 * @create: 2019年12月6日12:56:08
 */
@Data
public class BlogSortVO extends BaseVO<BlogSortVO> {

    /**
     * 分类名
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String sortName;

    /**
     * 分类介绍
     */
    private String content;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 无参构造方法
     */
    BlogSortVO() {

    }

}
