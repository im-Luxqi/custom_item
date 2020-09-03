package com.duomai.common.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

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


    public <T> T findBeautyAdmjson(Class<T> clazz) {
        return (T) JSON.parseObject(JSON.toJSONString(admjson),
                clazz);
    }

    public JSONObject findJsonObjectAdmjson() {
        return new JSONObject((Map<String, Object>) admjson);
    }
}
