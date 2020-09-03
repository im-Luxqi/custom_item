package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * Pv来源页
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-28 11:21
 */
public enum PvPageEnum implements IEnum {
    ACT_INDEX("ACT_INDEX", "活动首页"),
    ACT_LUCKY_DRAW("ACT_LUCKY_DRAW", "活动抽奖页"),
    ;

    private final String code;
    private final String descp;


    PvPageEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
