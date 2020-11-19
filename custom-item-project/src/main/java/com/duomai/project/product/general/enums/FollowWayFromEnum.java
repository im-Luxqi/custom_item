package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 关注途径
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum FollowWayFromEnum implements IEnum {
    UNDIFIND("UNDIFIND", "未知"),
    NON_FOLLOW("NON_FOLLOW", "尚未关注"),
    HISTROY_FOLLOW("HISTROY_FOLLOW", "老关注"),
    NATURE_JOIN_FOLLOW("NATURE_JOIN_FOLLOW", "自然关注"),
    INVITEE_JOIN_FOLLOW("INVITEE_JOIN_FOLLOW", "被邀请关注"),
    ;
    private final String code;
    private final String descp;

    FollowWayFromEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
