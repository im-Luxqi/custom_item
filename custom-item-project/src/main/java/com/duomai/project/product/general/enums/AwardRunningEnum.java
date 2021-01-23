package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 奖品发放状态
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum AwardRunningEnum implements IEnum {
    READY("READY", "准备"),
    RUNNING("RUNNING", "进行中"),
    FINISH("FINISH", "结束"),
    ;
    private final String code;
    private final String descp;

    AwardRunningEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
