package com.duomai.project.api.taobao.enums;

/** alibaba.benefit.query奖池信息查询，常见错误码
 * @description
 * 应用所有超过限制的网络计数  APPLY_ALL_NETWORK_COUNT_EXCEED_LIMIT
 * 应用并发锁 APPLY_CONCURRENT_LOCKED
 * 适用一卖家计数超过限额 APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT
 * 适用一卖家计数超过限额 NO_RIGHT_QUANTITY
 * @create by 王星齐
 * @time 2020-08-24 10:06:25
 */
public enum TaoBaoSendCouponStatus {
    COUPON_INVALID_OR_DELETED("COUPON_INVALID_OR_DELETED", "等待买家付款"),
    APPLY_OWNSELF_COUPON("APPLY_OWNSELF_COUPON", "商家不能把自己的权益发给自己的帐号"),
    APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT("APPLY_SINGLE_COUPON_COUNT_EXCEED_LIMIT", "用户该类型权益已达上限"),
    OFF_LINE("权益已下线", "权益已下线"),
    LOCK311("311权益已经被锁定", "权益邦定了店铺宝活动后，只能通过店铺店活动发放权益，其它方式无法发放，店铺宝权益和其它活动权益分开即可"),
    APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT("APPLY_ONE_SELLER_COUNT_EXCEED_LIMIT", "用户优惠券超出10张限制"),
    ERRORSERVER_INVOKE_MTEE_EXCEPTION("ERRORserver-invoke-mtee-exception", "没查到，提供的错误准吗？"),
    NO_RIGHT_QUANTITY("NO_RIGHT_QUANTITY", "权益库存不足"),
    ERRORA_3_567("ERROR A_3_567", "ERROR A_3_567这些用户的账号都存在风险，所以被拦截了，做安全防控的原因主要也是担心商家的权益发放被黑灰产盗刷"),
    ERRORA_3_00_005("ERROR A_3_00_005", "ERROR A_3_00_005这些用户的账号都存在风险，所以被拦截了，做安全防控的原因主要也是担心商家的权益发放被黑灰产盗刷"),
    ERRORA_3_00_002("ERROR A_3_00_002", "ERROR A_3_00_002这些用户的账号都存在风险，所以被拦截了，做安全防控的原因主要也是担心商家的权益发放被黑灰产盗刷"),
    ;


    private  final  String code;
    private final String info;

    TaoBaoSendCouponStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
