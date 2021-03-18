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
    CARD_ONE("CARD_ONE", "1号卡片"),
    CARD_TWO("CARD_TWO", "2号卡片"),
    CARD_THREE("CARD_THREE", "3号卡片"),
    CARD_FOUR("CARD_FOUR", "4号卡片"),
    CARD_FIVE("CARD_FIVE", "5号卡片"),
    CARD_SIX("CARD_SIX", "6号卡片"),
    CARD_SEVEN("CARD_SEVEN", "7号卡片"),
    CARD_EIGHT("CARD_EIGHT", "8号卡片"),
    CARD_NINE("CARD_NINE", "9号卡片"),
    POOL("POOL", "奖池");
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


    public static AwardUseWayEnum randomType(AwardUseWayEnum[] values) {
        AwardUseWayEnum value = values[(int) (Math.random() * values.length)];
        if (POOL.equals(value)) {
            value = randomType(values);
        }
        return value;
    }

}