package com.duomai.starter;

import lombok.Data;

/** 客户的一些必要配置
 * @description
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
