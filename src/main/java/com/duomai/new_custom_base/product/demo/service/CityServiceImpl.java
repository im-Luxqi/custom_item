package com.duomai.new_custom_base.product.demo.service;

import com.duomai.new_custom_base.framework.service.impl.BaseServiceImpl;
import com.duomai.new_custom_base.product.demo.domain.City;
import com.duomai.new_custom_base.product.demo.mapper.CityMapper;
import org.springframework.stereotype.Service;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-27 16:12
 */
@Service
public class CityServiceImpl extends BaseServiceImpl<CityMapper, City> implements CityService {
}
