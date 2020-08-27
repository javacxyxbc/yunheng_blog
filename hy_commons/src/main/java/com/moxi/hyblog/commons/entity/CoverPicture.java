package com.moxi.hyblog.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.hyblog.base.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/17 21:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coverpicture")
public class CoverPicture  extends SuperEntity<CoverPicture> {
    private String sortUid;
    private String url;
}
