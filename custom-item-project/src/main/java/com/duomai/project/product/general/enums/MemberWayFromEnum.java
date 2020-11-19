package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 入会途径
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum MemberWayFromEnum implements IEnum {
    NON_MEMBER("NON_MEMBER", "非会员"),
    HISTROY_MEMBER("HISTROY_MEMBER", "老会员"),
    NATURE_JOIN_MEMBER("NATURE_JOIN_MEMBER", "自然入会"),
    INVITEE_JOIN_MEMBER("INVITEE_JOIN_MEMBER", "被邀请入会"),
    ;
    private final String code;
    private final String descp;

    MemberWayFromEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
