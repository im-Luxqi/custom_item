package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

public enum CusBigWheelStateEnum implements IEnum {
    NOT_STARTING("NOT_STARTING","未开始"),
    PROGRESSING("PROGRESSING","进行中"),
    STOPPING("STOPPING","已结束")
    ;

    CusBigWheelStateEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    private final String code;
    private final String descp;

    @Override
    public String getValue() {
        return code;
    }
}
