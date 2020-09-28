package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 游戏基本设置
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-26 19:22
 */
@Data
@Accessors(chain = true)
public class ActBaseSettingDto {
    /*
     * 活动规则
     **/
    private String actRule;
    /*
     * 活动开始时间
     **/
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date actStartTime;

    /*
     * 活动结束时间
     **/
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date actEndTime;


}
