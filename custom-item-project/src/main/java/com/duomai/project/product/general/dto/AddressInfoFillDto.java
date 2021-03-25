package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 留资
 *
 * @description Bean Validation 中内置的 constraint
 * @Null 被注释的元素必须为 null
 * @NotNull 被注释的元素必须不为 null
 * @AssertTrue 被注释的元素必须为 true
 * @AssertFalse 被注释的元素必须为 false
 * @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @Size(max=, min=)   被注释的元素的大小必须在指定的范围内
 * @Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内
 * @Past 被注释的元素必须是一个过去的日期
 * @Future 被注释的元素必须是一个将来的日期
 * @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
 * Hibernate Validator 附加的 constraint
 * @NotBlank(message =)   验证字符串非null，且长度必须大于0
 * @Email 被注释的元素必须是电子邮箱地址
 * @Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
 * @NotEmpty 被注释的字符串的必须非空
 * @Range(min=,max=,message=) 被注释的元素必须在合适的范围内
 * @create by 王星齐
 * @time 2020-11-08 19:38:42
 */
@Data
@Accessors(chain = true)
public class AddressInfoFillDto {

    @NotBlank(message = "获奖日志ID不能为空")
    private String id;

    @NotBlank(message = "收件人昵称 不能为空")
    @Length(max = 15, message = "最大不超过15个字")
//    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,14}$", message = "请输入汉字,且最大不超过15个字")
    private String receviceName;
    @NotBlank(message = "收件人电话 不能为空")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "请输入正确的手机号码")
    private String recevicePhone;
//    @NotBlank(message = "收件人地址 市 receviceCity不能为空")
//    @Length(max = 64, message = "收件人地址 市 不超过64个字符")
    private String receviceCity;
    @NotBlank(message = "收件人地址 省 receviceProvince不能为空")
    @Length(max = 64, message = "收件人地址 省 不超过64个字符")
    private String receviceProvince;
    @NotBlank(message = "收件人地址 区 receviceDistrict不能为空")
    @Length(max = 64, message = "收件人地址 区 不超过64个字符")
    private String receviceDistrict;
    @Length(max = 140, message = "详细地址不超过140个字符")
    @NotBlank(message = "收件人地址 详细地址 不能为空")
    private String receviceAddress;


//    @Pattern(regexp = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$", message = "身份证格式不正确")
//    @Pattern(regexp = "^[A-Za-z0-9]{15,19}$", message = "身份证格式不正确,请输入15-18位数字或字母")
//    @NotBlank(message = "身份证 不能为空")
    private String identityNumber;

}