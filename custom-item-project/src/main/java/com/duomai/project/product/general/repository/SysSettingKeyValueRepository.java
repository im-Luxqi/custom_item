package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysSettingKeyValue;

import java.util.List;

public interface SysSettingKeyValueRepository extends BaseRepository<SysSettingKeyValue, String> {

    //根据类型获取对应集合
    List<SysSettingKeyValue> findByType(String type);

}
