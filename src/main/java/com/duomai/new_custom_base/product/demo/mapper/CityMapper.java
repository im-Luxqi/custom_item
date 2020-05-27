package com.duomai.new_custom_base.product.demo.mapper;

import com.duomai.new_custom_base.framework.mapper.BaseMapper;
import com.duomai.new_custom_base.product.demo.domain.City;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper extends BaseMapper<City> {
}
