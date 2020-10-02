package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/* 奖品使用途径
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 **/
public enum AwardUseWayEnum implements IEnum {
    POOL("POOL", "奖池抽奖"),
    FIRSTLUCKY("FIRSTLUCKY", "首次抽奖必中"),
    INVITE("INVITE", "邀请人员直接发放")
    ;
    private final String code;
    private final String descp;

    AwardUseWayEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}