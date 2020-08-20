package com.duomai.project.product.demo.service;

import com.duomai.project.framework.mybatisplus.service.impl.BaseServiceImpl;
import com.duomai.project.product.demo.domain.City;
import com.duomai.project.product.demo.mapper.CityMapper;
import org.springframework.stereotype.Service;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-27 16:12
 */
@Service
public class CityServiceImpl extends BaseServiceImpl<CityMapper, City> implements CityService {
}
