package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/* 奖品类型
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 **/
public enum AwardTypeEnum implements IEnum {
    GOODS("GOODS", "实物"), COUPON("COUPON", "优惠券");;
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
