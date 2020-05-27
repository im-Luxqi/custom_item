package com.duomai.new_custom_base.product.gen.service;

import com.duomai.new_custom_base.framework.service.impl.BaseServiceImpl;
import com.duomai.new_custom_base.product.gen.domain.GenTable;
import com.duomai.new_custom_base.product.gen.mapper.GenTableMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 业务 服务层实现
 *
 * @author Crown
 */
@Service
@Slf4j
public class GenTableServiceImpl extends BaseServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

}