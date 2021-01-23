package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwSetting;

import java.util.List;

public interface XhwSettingRepository extends BaseRepository<XhwSetting, String> {

    List<XhwSetting> findByType(String typeActSetting);

    XhwSetting findFirstByK(String join_num);
}
