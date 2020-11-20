package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * 奶瓶类型
 *
 * @description
 * @create by 王星齐
 */
public enum BottleTypeEnum implements IEnum {
    COMMON_ONE("COMMON_ONE", "普通1号奶瓶"),
    COMMON_TWO("COMMON_TWO", "普通2号奶瓶"),
    COMMON_THREE("COMMON_THREE", "普通3号奶瓶"),
    COMMON_FOUR("COMMON_FOUR", "普通4号奶瓶"),
    SPECIAL_FIVE("SPECIAL_FIVE", "特殊5号奶瓶"),
    SPECIAL_SIX("SPECIAL_SIX", "特殊6号奶瓶"),
    ;

    private final String code;
    private final String descp;


    BottleTypeEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
