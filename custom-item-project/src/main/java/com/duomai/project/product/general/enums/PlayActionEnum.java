package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/** 当前行动
 * @description
 * @create by 王星齐
 * @time 2020-08-28 14:28:07
 */
public enum PlayActionEnum implements IEnum {
    playwith_snowman("playwith_snowman", "雪人"),
    playwith_penguin("playwith_penguin", "企鹅"),
    playwith_bear("playwith_bear", "熊"),
    letter_party2("letter_party2", "场景2邀请函"),
    playwith_lamp("playwith_lamp", "灯"),
    playwith_tent("playwith_tent", "帐篷"),
    playwith_dog("playwith_dog", "狗"),
    letter_party3("letter_party3", "场景3邀请函"),
    party3_ing("party3_ing", "场景3进行中"),
    ;
    private final String code;
    private final String descp;

    PlayActionEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
