package main.commons.vo;

import lombok.Data;
import main.base.validator.annotion.NotBlank;
import main.base.validator.group.Insert;
import main.base.vo.BaseVO;
import org.apache.ibatis.annotations.Update;

/**
 * BlogVO
 *
 * @author: 陌溪
 * @create: 2019年12月4日12:26:36
 */
@Data
public class TagVO extends BaseVO<TagVO> {

    /**
     * 标签内容
     */
    @NotBlank(groups = {Insert.class, Update.class})
    private String content;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 无参构造方法，初始化默认值
     */
    TagVO() {

    }

}
