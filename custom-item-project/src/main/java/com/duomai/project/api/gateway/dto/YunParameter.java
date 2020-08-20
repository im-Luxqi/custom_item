package com.duomai.project.api.gateway.dto;


import lombok.Data;

/**
 * 云应用系统参数
 **/
@Data
public class YunParameter {

    /**
     * 商家应用的appKey
     */
    private String YunAppKey;

    /**
     * 当前登录用户的昵称(需要用户授权)
     */
    private String userNick;

    /**
     * 当前登录用户的openId
     */
    private String openId;

    /**
     * 当前云应用调用环境（test或者online）
     */
    private String env;

    /**
     * 当前小程序的id
     */
    private String miniAppId;

    /**
     * 当前登录用户的授权token（需要用户授权）
     */
    private String accessToken;

    /**
     * 使用当前小程序appkey和secret进行对参数进行加签后的签名（小程序给的）
     */
    private String sign;

}
