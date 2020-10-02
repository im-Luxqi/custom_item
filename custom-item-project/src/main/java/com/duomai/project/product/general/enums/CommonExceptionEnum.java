package com.duomai.project.product.general.enums;


/**
 * @内容：错误集合
 */
public enum CommonExceptionEnum {

    //系统级异常
    OPERATION_SUCCESS("00000","操作成功!"),
    OPERATION_ERROR("11111","操作失败!"),

    //参数级异常
    USER_ID_ERROR("A0400","用户id不能为空!"),
    ACT_ID_ERROR("A0401","活动id不能为空!"),
    BUYER_NICK_ERROR("A0402","粉丝昵称不能为空!"),
    USER_ERROR("A0403","未查到用户信息!"),
    TYPE_ERROR("A0404","统计类型不能为空!"),
    TYPE_DISCREPANCY_ERROR("A0405","统计类型不符!"),
    NUM_ID_ERROR("A0406","宝贝id不能为空!"),
    IS_GRADE_ERROR("A0407","会员等级不能为空!"),
    IS_REL_ERROR("A0408","是否关注店铺不能为空!"),
    PROVINCE_ERROR("A0409","省份不能为空!"),
    TASK_LOG_TYPE_ERROR("A0410","任务类型不符!"),
    INVITER_NICK_NOT_NULL_ERROR("A0411","邀请人昵称不能为空!"),
    TASK_LOG_TYPE_NOT_NULL_ERROR("A0412","任务类型不能为空!"),
    ACTIVITY_DISPOSE_ERROR("A0413","未查询到活动配置表信息!"),
    GOODS_TYPE_NOT_NULL_ERROR("A0414","宝贝类型不能为空!"),
    ACTIVITY_STATE_ERROR("A0415","活动状态不能为空！"),
    NOT_FOUND_ACTIVITY_ERROR("A0416","未查询到活动信息!"),
    ACTIVITY_NOT_STARTED("A0417","亲，活动尚未开始呢!"),
    ACTIVITY_FINISHED("A0418","亲，活动已经结束了呢!"),
    FANS_INFORMATION_NOT_FOUND("A0419","粉丝信息不存在!"),
    TRY_SHOP_NOT_FOUND("A0420","试用商品不存在!"),
    BIZ_EXT_STRING_NOT_NULL("A0421","分享参数不能为空!"),
    ACTIVITY_DISPOSE_NOT_NULL_ERROR("A0422","活动配置表信息不能为空哦!"),
    HELPED_INVITEE_ERROR("A0423","亲、您已经帮助过别人了"),


    //方法级异常
    SYSTEM_ERROR("M00001","系统异常..请稍后再试!"),
    ACT_BASE_SETTING_FIND_ERROR("M00002","系统异常..获取活动基础信息失败!"),
    FIND_BY_BUYER_NICK_ERROR("M00003","系统异常..获取粉丝信息失败!"),
    UN_USE_LUCKY_CHANCE_ERROR("M00004","系统异常..获取抽奖次数失败!"),
    get_Finish_The_Task_Num_error("M00005","系统异常..查询签到日志失败!"),
    COUNT_BY_INVITER_ERROR("M00006","系统异常..查询邀请日志失败!"),
    GET_INVITER_LOG_ERROR("M00007","系统异常..查询邀请日志失败!"),

    //外接口调用异常
    TAO_BAO_ERROR("B00001","淘宝接口异常!")
    ;

    CommonExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
