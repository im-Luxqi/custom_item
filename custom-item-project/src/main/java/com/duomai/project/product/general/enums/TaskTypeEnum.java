package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/* 任务类型
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 **/
public enum TaskTypeEnum implements IEnum {
    MEMBER("MEMBER", "入会"),
    SIGN("SIGN", "签到"),
    FOLLOW("FOLLOW", "关注")
    ;
    private final String code;
    private final String descp;

    TaskTypeEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}