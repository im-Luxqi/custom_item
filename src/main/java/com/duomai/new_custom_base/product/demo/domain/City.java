package com.duomai.new_custom_base.product.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-27 16:05
 */
@Data
public class City {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("city_name")
    private String cityName;
    @TableField("parent_id")
    private Integer parentId;
    @TableField("parent_name")
    private String parentName;
}
