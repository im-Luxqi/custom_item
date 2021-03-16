package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 抽奖机会来源
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum LuckyChanceFromEnum implements IEnum {
    FREE("FREE", "赠送"),
    SIGN("SIGN", "签到"),
    FOLLOW("FOLLOW", "关注"),
    MEMBER("MEMBER", "入会"),
    SHARE("SHARE", "分享"),
    SHARE_FOLLOW("SHARE_FOLLOW", "邀请关注店铺"),
    SHARE_MEMBER("SHARE_MEMBER", "邀请加入会员"),
    BROWSE("BROWSE", "浏览"),
    TV("TV", "观看直播"),
    ORDER("ORDER", "下单"),
    ;
    private final String code;
    private final String descp;

    LuckyChanceFromEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
