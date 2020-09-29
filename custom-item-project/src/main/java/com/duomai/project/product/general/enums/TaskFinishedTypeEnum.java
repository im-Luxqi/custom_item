package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

public enum TaskFinishedTypeEnum implements IEnum {
    FINISHED("FINISHED","已完成"), NOT_FINISHED("NOT_FINISHED","未完成")
    ;

    private final String code;
    private final String descp;

    TaskFinishedTypeEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
