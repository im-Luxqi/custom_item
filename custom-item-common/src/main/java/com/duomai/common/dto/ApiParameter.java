package com.duomai.common.dto;


import lombok.Data;

import javax.validation.Valid;

/**
 * API请求参数
 **/
@Data
public class ApiParameter {

    /**
     * 公共参数
     */
    @Valid
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
