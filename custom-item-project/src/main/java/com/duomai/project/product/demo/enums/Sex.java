package com.duomai.project.product.demo.enums;


import com.duomai.common.base.enums.IEnum;

public enum Sex implements IEnum {
    Man("Man", "男"), Female("Female", "女"),
    ;

    private final String code;
    private final String descp;

    Sex(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
