package com.duomai.common.dto;

import lombok.Data;

@Data
public class ReturnBaseData {

    private Integer status;
    private Object data;
    private String msg;

    //通用成功码 500
    public static Integer success = 200;
    //通用错误 500
    public static Integer error = 500;

    public ReturnBaseData(Integer status, Object data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }
}
