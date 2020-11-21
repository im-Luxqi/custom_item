package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 抽奖机会来源
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum LuckyChanceFromEnum implements IEnum {
    FREE("FREE","首次进游戏免费送"),
    SIGN("SIGN", "每日签到"),
    INVITE_MEMBER("INVITE_MEMBER", "邀请入会"),
    SHARE("SHARE", "分享"),
    MEMBER("MEMBER", "入会"),
    FOLLOW("FOLLOW", "关注店铺"),
    BROWSE("BROWSE", "浏览商品"),
    TV("TV", "看直播"),
    ORDER("TV", "下单"),
    DAKA("DAKA", "大咖日历"),
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
