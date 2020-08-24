package com.duomai.project.product.demo.enums;


import com.duomai.common.base.enums.IEnum;

public enum Sex implements IEnum {
    Man(1, "男"), Female(0, "女"),
    ;

    private final int code;
    private final String descp;

    Sex(Integer code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public int getValue() {
        return code;
    }
}
