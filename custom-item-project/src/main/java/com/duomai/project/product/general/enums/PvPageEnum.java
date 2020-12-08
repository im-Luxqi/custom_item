package com.duomai.project.product.general.enums;

import com.duomai.common.base.enums.IEnum;

/**
 * Pv来源页
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-28 11:21
 */
public enum PvPageEnum implements IEnum {
    PAGE_INDEX("PAGE_INDEX", "首页"),
    PAGE_PARTY1("PAGE_PARTY1", "场景1"),
    PAGE_PARTY2("PAGE_PARTY2", "场景2"),
    PAGE_PARTY3("PAGE_PARTY3", "场景3"),
    ;

    private final String code;
    private final String descp;


    PvPageEnum(String code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @Override
    public String getValue() {
        return code;
    }
}
