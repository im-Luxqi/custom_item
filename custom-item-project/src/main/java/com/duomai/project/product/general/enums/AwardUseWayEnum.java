package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * 奖品使用途径
 *
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum AwardUseWayEnum implements IEnum {
    COMMON_ONE("COMMON_ONE", "普通1号奶瓶"),
    SPECIAL_FIVE("SPECIAL_FIVE", "特殊5号奶瓶"),


    PENGUIN("PENGUIN", "企鹅奖池"),
    PARTY2("PARTY2", "party2开场奖池"),
    POOL("POOL", "一般奖池"),
    TENT("TENT", "帐篷奖池"),


    DOG_FIRST("DOG_FIRST", "第一次和狗互动"),
    BEAR_FIRST("BEAR_FIRST", "第一次和熊互动"),


    RANKING1("RANKING1", "排行榜1"),
    RANKING2("RANKING2", "排行榜2"),
    RANKING3("RANKING3", "排行榜3"),
    RANKING4("RANKING4", "排行榜4"),
    RANKING5("RANKING5", "排行榜5"),
    RANKING6("RANKING6", "排行榜6"),
    RANKING7("RANKING7", "排行榜7"),
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