package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * 任务类型
 *
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum TaskTypeEnum implements IEnum {
    SIGN("SIGN", "签到"),
    FOLLOW("FOLLOW", "关注"),
    MEMBER("MEMBER", "入会"),
    SHARE("SHARE", "分享"),
    SHARE_FOLLOW("SHARE_FOLLOW", "邀请关注店铺"),
    SHARE_MEMBER("SHARE_MEMBER", "邀请加入会员"),
    BROWSE("BROWSE", "浏览"),
    TV("TV", "观看直播"),
    SPEND("SPEND", "消费"),
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