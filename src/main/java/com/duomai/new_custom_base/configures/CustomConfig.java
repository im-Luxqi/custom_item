package com.duomai.new_custom_base.configures;

import lombok.Data;

/**
 * @description
 * @create by 王星齐
 * @date 2020-08-19 9:19
 */
@Data
public class CustomConfig {

    /**
     * 例： 30482173
     */
    private String appkey;
    /**
     * 例： d572b124d2d70b648a5e7e8790959a70
     */
    private String secretkey;
    /**
     * 例： promotioncenter-3000000019832114
     */
    private String appName;
    /**
     * 例： 50000000b11gmdOAc0owgha19c51b2cl0hmTCuTgZEar8KW2EoDJnYeSwpGTQ0qoC00
     */
    private String sessionkey;
}
