package com.moxi.hyblog.xo.vo;

import com.moxi.hyblog.base.validator.annotion.NotBlank;
import com.moxi.hyblog.base.validator.group.Insert;
import com.moxi.hyblog.base.validator.group.Update;
import com.moxi.hyblog.base.vo.BaseVO;
import lombok.Data;

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
