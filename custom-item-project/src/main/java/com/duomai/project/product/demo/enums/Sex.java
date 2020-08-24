package com.duomai.project.product.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Sex {
    Man(1, "男"), Female(0, "女"),
    ;

    @EnumValue
    private final Integer code;
    private final String descp;

    Sex(Integer code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescp() {
        return descp;
    }
}
