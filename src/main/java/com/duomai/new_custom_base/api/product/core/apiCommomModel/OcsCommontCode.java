package com.duomai.new_custom_base.api.product.core.apiCommomModel;

/**
 * @Description:
 * @Author: szt
 * @Date: 2019/3/21 0021  11:51
 */
public enum OcsCommontCode {

    /**********************存储类型***********************/

    /**appkey对应密钥管理表*/
    APPK_MYGL(0,"appkmygl"),

    /**appkey 与 接口id*/
    APPK_APIID(1,"appkapiid"),

    /**接口id 和权限*/
    APPID_LEVEL(2,"appidlevel"),

    /**appkey 单独加入缓存*/
    APPK(3,"appk"),

    /**sign  签名时效性*/
    SIGN(4,"sign"),

    /**白名单ip*/
    WHITE_IP(5,"whiteIp"),

    /**集合ip对应黑白名单状态*/
    LIST_IP(6,"listIp"),

    /**集合所有API类型*/
    LIST_API_TYPE(11,"listIp"),


    /************************** key值**************************/

    /**集合Appkey对应的secretkey,缓存中固定的key值*/
    LIST_APPKEY(7,"listAppkey"),

    /**集合所有API,缓存中固定的key值*/
    LIST_APIKey(8,"listApi"),

    /**集合所有code,缓存中固定的key值*/
    LIST_CodeKey(12,"listCode"),

    /**
     * 缓存中对应的请求状态 无数据
     */
    NO_DATA(9,"nodata"),

    /**
     * 缓存中对应的请求状态 异常
     */
    EXP(10,"exp");

    private Integer code;
    private String message;

    OcsCommontCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过code 取枚举
     * @param value
     * @return
     */
    public static OcsCommontCode getEnumByCode(Integer code){
        for (OcsCommontCode enums : OcsCommontCode.values()) {
            if (enums.getCode() == code) {
                return enums;
            }
        }
        return null;
    }

    /**
     * 通过code 取值
     * @return
     */
    public static String getMsgByCode(Integer code) {
        for (OcsCommontCode enums : OcsCommontCode.values()) {
            if (enums.getCode() == code) {
                return enums.getMessage();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
