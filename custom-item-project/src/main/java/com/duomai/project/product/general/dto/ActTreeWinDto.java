package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class ActTreeWinDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 点亮圣诞数的星源值
     */
    private Integer starValueTreeLimit;


    /**
     * 点亮圣诞树时间限制
     */
    private Date timeTreeLimit;


    /**
     * 排行榜奖励1
     */
    private String treeAwardOne;

    /**
     * 排行榜奖励2
     */
    private String treeAwardTwo;

    /**
     * 排行榜奖励3
     */
    private String treeAwardThree;


    /**
     * 排行榜奖励4
     */
    private String treeAwardFour;


}
