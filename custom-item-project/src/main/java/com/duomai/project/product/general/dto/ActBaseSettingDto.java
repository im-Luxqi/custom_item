package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 游戏基本设置
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @date 2020-08-26 19:22
 */
@Data
@Accessors(chain = true)
public class ActBaseSettingDto {
    /**
     * 活动规则
     */
    private String actRule;
    /**
     * 活动开始时间
     */
    private Date actStartTime;

    /**
     * 活动结束时间
     */
    private Date actEndTime;


    /**
     * 优惠券一人可以抽几张
     */
    private Integer drawCouponNum;


    /**
     * 每连续签到几天,有额外奖励
     */
    private Integer taskSignContinuous;


    /**
     * 每连续签到几天,那天的奖励（默认一次）
     */
    private Integer taskSignContinuousPayment;

    /**
     * 完成浏览任务，需要浏览几个商品
     */
    private Integer taskBrowseShouldSee;


}
