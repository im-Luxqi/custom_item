package com.duomai.project.product.adidasmusic.service.impl;

import com.duomai.common.framework.mybatisplus.service.impl.BaseServiceImpl;
import com.duomai.project.product.adidasmusic.domain.CusBigWheel;
import com.duomai.project.product.adidasmusic.mapper.CusBigWheelMapper;
import com.duomai.project.product.adidasmusic.service.ICusBigWheelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【请填写功能名称】 服务层实现
 *
 * @author system
 */
@Service
public class CusBigWheelServiceImpl extends BaseServiceImpl<CusBigWheelMapper, CusBigWheel> implements ICusBigWheelService {
    @Resource
    private CusBigWheelMapper mapper;

    @Override
    public List<CusBigWheel> listCusBigWheel() {
        return mapper.listCusBigWheel();
    }
}
