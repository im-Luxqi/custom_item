package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysKeyValue;

import java.util.List;

public interface SysKeyValueRepository extends BaseRepository<SysKeyValue, String> {

    //根据类型获取对应集合
    List<SysKeyValue> findByType(String type);

}
