package com.moxi.hyblog.xo.vo;

import com.moxi.hyblog.base.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/17 21:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoverPictureVO extends BaseVO<CoverPictureVO> {
    private String uid;
    private String sortUid;
}
