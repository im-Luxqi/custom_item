package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * @邀请类型
 */
public enum InvitationTypeEnum implements IEnum {

    memberStage("memberStage", "会员阶段"),
    invitationStage("invitationStage", "邀请阶段"),
    ;
    private final String code;
    private final String desc;

    InvitationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return code;
    }
}