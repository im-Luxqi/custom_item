package com.duomai.project.product.demo.domain;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.duomai.project.product.demo.enums.Sex;
import lombok.Getter;
import lombok.Setter;

/**
 * 【请填写功能名称】表 city
 *
 * @author system
 */
@Setter
@Getter
public class City {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     *
     */
    private String cityName;
    /**
     *
     */
    private Long parentId;
    /**
     *
     */
    private String parentName;

    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private Sex sex;
}
