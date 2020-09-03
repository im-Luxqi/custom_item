package com.duomai.project.product.general.dto;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @create by 王星齐
 * @date 2020-09-01 9:01
 */
@Data
public class XyReturn {

    private Integer code;
    private List<XyData> data;
    private String msg;

}
