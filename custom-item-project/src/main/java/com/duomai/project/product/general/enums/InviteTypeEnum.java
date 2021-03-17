package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * 任务类型
 *
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum InviteTypeEnum implements IEnum {
    INVITE_COMMON("INVITE_COMMON", "邀请"),
    INVITE_FOLLOW("INVITE_FOLLOW", "邀请关注"),
    INVITE_MEMBER("INVITE_MEMBER", "邀请入会"),
    ;
    private final String code;
    private final String descp;

    InviteTypeEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}