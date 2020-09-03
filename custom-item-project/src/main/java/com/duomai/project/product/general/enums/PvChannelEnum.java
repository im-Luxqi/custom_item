package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * pv 来源渠道
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-28 11:22
 */
public enum PvChannelEnum implements IEnum {
    ADIDAS_SHOP_INDEX("ADIDAS_SHOP_INDEX", "阿迪官方旗舰店首页");

    private final String code;
    private final String descp;

    PvChannelEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
