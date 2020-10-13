package com.duomai.project.product.adidasmusic.enums;

/* 奖品等级
 * @description
 * @create by 王星齐
 * @time 2020-10-10 16:07:46
 **/
public enum PoolLevelEnum {
    LEVEL_1(1, "等级1"),
    LEVEL_2(2, "等级2"),
    LEVEL_3(3, "等级3"),
    LEVEL_4(4, "等级4"),
    LEVEL_5(5, "等级5");
    private final Integer code;
    private final String descp;

    PoolLevelEnum(Integer code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    public Integer getValue() {
        return code;
    }
}
