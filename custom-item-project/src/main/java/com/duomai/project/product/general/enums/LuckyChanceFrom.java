package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/* 抽奖机会来源
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 **/
public enum LuckyChanceFrom implements IEnum {
    ORDER_COMMIT("ORDER_COMMIT", "订单提交"), ORDER_INVALID("ORDER_INVALID", "无效订单,每天只有第一单订单有效");
    private final String code;
    private final String descp;

    LuckyChanceFrom(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
