package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class XhwSettingDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 活动规则
     */
    private String actRule;

    /**
     * 虚拟人数
     */
    private String virtualNum;
}
