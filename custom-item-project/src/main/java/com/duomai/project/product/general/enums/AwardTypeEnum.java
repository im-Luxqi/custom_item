package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * 奖品类型
 *
 * @description
 * @create by 王星齐
 */
public enum AwardTypeEnum implements IEnum {
    GOODS("GOODS", "实物"),
    COUPON("COUPON", "优惠券"),
    MEMBER_POINTS("MEMBER_POINTS", "会员积分"),
    EXCHANGE("EXCHANGE", "兑换奖品");
    private final String code;
    private final String descp;

    AwardTypeEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
