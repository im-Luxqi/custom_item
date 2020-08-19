package com.duomai.new_custom_base.api.product.core.pojo;

import lombok.Data;

@Data
public class ReturnBaseData {

    private Integer status;
    private Object data;
    private String msg;

    //通用成功码 500
    public static Integer code = 200;
    //通用错误 500
    public static Integer errorCode = 500;
    //调用淘宝出错
    public static Integer taoBaoCode = 501;
    //1/未关注店铺
    public static Integer followShop = 12021;
    //未入会
    public static Integer membership = 12022;
    //2/积分不足
    public static Integer notPoint = 12023;
    //4/等级不符
    public static Integer gradeDiscrepancy = 12024;
    // 5/申请不通过
    public static Integer applicationFailed = 12025;


    public ReturnBaseData() {
    }

    public ReturnBaseData(Integer status, Object data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public ReturnBaseData(Object data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public ReturnBaseData(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ReturnBaseData(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ReturnBaseData(Integer status) {
        this.status = status;
    }

}
