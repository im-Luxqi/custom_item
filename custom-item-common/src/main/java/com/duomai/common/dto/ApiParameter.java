package com.duomai.common.dto;


import lombok.Data;

/**
 * API请求参数
 **/
@Data
public class ApiParameter {

    /**
     * 公共参数
     */
    private CommomParameter commomParameter;

    /**
     * 云应用系统参数
     */
    private YunTokenParameter yunTokenParameter;

    /**
     * 业务参数
     */
    private Object admjson;
}
