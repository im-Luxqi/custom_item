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
    PAGE_INDEX("PAGE_INDEX", "活动首页"),
    PAGE_BROWSE("PAGE_BROWSE", "浏览页面"),
    PAGE_DAKA("PAGE_DAKA", "大咖专属活动子入口"),
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
