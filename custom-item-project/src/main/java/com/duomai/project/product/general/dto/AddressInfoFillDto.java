package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 留资信息
 */
@Data
@Accessors(chain = true)
public class AddressInfoFillDto {

    @NotBlank(message = "获奖日志ID不能为空")
    private String id;

    @NotBlank(message = "收件人昵称 不能为空")
    @Size(max = 64, message = "收件人昵称 不超过64个字符")
    private String receviceName;
    @NotBlank(message = "收件人电话 不能为空")
    @Size(max = 64, message = "收件人电话 不超过64个字符")
    private String recevicePhone;
    //    @NotBlank(message = "收件人地址 市 不能为空")
    private String receviceCity;
    @NotBlank(message = "收件人地址 省 不能为空")
    @Size(max = 64, message = "收件人地址 省 不超过64个字符")
    private String receviceProvince;
    //    @NotBlank(message = "收件人地址 区 不能为空")
    private String receviceDistrict;
    @Size(max = 140, message = "详细地址不超过140个字符")
    @NotBlank(message = "收件人地址 详细地址 不能为空")
    private String receviceAddress;

}