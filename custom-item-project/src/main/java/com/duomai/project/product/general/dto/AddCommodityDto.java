package com.duomai.project.product.general.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddCommodityDto {

    private String name;
    private Long numId;
    private String type;
    private String img;
    private String price;
    private Date createTime;
    private String commoditySort;

}
