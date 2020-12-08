package com.duomai.project.helper.constants;

public interface ActSettingConstant {

    String TYPE_ACT_SETTING = "type_act_setting";
    /**
     * 活动规则
     */
    String ACT_RULE = "act_rule";
    /**
     * 活动开始时间
     */
    String ACT_START_TIME = "act_start_time";
    /**
     * 活动结束时间
     */
    String ACT_END_TIME = "act_end_time";

    /**
     * 活动最后一天
     */
    String ACT_LAST_TIME = "act_last_time";


    /**
     * 订单开始时间
     */
    String ORDER_START_TIME = "order_start_time";

    /**
     * 订单结束时间
     */
    String ORDER_END_TIME = "order_end_time";







    /**
     * 优惠券可抽奖次数
     */
    String DRAW_COUPON_NUM = "draw_coupon_num";

    /**
     * 每连续签到几天,有额外奖励
     */
    String TASK_SIGN_CONTINUOUS = "task_sign_continuous";

    /**
     * 每连续签到几天,那天的奖励（默认一次）
     */
    String TASK_SIGN_CONTINUOUS_PAYMENT = "task_sign_continuous_payment";


    /**
     * 完成浏览任务，需要浏览几个商品
     */
    String TASK_BROWSE_SHOULD_SEE = "task_browse_should_see";


    /**
     * 下单任务，满多少
     */
    String TASK_ORDER_SHOULD_SPEND = "task_order_should_spend";


}
