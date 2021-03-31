package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class ActBaseSettingDto implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * 活动结束时间
     */
    private String actMusicUrl;


    /**
     * 优惠券一人可以抽几张
     */
    private Integer drawCouponNum;


    /**
     * 累计签到奖励（json）
     */
    private List<SignWinDto> taskSign;

    /**
     * 完成浏览任务，需要浏览几个商品
     */
    private Integer taskBrowseShouldSee;


    /**
     * 完成一次分享任务需要邀请几个人
     */
    private Integer taskShareShould;



    /**
     * 完成一次分享关注任务需要邀请几个人
     */
    private Integer taskShareFollowShould;
}
