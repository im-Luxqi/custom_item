package com.duomai.project.product.adidasmusic.dto;

import com.duomai.project.product.adidasmusic.enums.PoolLevelEnum;
import lombok.Data;

/**
 * @description
 * @create by 王星齐
 * @date 2020-10-10 16:28
 */
@Data
public class CustomPlayProgressDto {

    private Integer signNum;
    private Integer orderNum;
    private PoolLevelEnum currentPoolLevel;
}
