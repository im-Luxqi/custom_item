package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 奖品类型
 * @description
 * @create by 王星齐
 */
public enum AwardTypeEnum implements IEnum {
    GOODS("GOODS", "实物"),
    COUPON("COUPON", "优惠券"),
    ITEM("ITEM", "单品券"),
    EXCHANGE("EXCHANGE", "可用于兑换的虚拟货币")
    ;
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
