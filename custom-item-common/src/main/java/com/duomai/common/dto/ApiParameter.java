package com.duomai.common.dto;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "业务参数不能为空")
    private Object admjson;
}
