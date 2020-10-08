package com.duomai.project.product.general.dto;

import com.duomai.project.product.general.entity.SysAward;
import lombok.Data;

/**
 * @description 签到天数对应的奖品
 * @author 多卖
 */
@Data
public class SignDto {

    //签到天数
    private Integer num;

    //奖品信息
    private SysAward sysAward;

    //是否领取 0 未领取 1 已领取
    private Integer isSend;

}
