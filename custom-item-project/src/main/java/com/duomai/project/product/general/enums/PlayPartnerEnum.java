package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 互动对象
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum PlayPartnerEnum implements IEnum {
    snowman("snowman", "雪人"),
    penguin("penguin", "企鹅"),
    bear("bear", "熊"),
    lamp("lamp", "灯"),
    tent("tent", "帐篷"),
    dog("dog", "狗"),
    partybrowse("partybrowse", "场景三浏览"),
    balloon("balloon", "气球"),
    ;
    private final String code;
    private final String descp;

    PlayPartnerEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
