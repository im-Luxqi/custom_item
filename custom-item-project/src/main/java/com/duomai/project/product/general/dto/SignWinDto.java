package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到奖励
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @date 2020-08-26 19:22
 */
@Data
@Accessors(chain = true)
public class SignWinDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 累计签到天数
     */
    private Integer totalDay;
    /**
     * 获得碎片的数量
     */
    private Integer cardNum;

}
