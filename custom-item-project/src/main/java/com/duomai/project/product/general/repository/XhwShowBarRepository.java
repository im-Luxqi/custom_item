package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.entity.XhwShowBar;

import java.util.List;

public interface XhwShowBarRepository extends BaseRepository<XhwShowBar, String> {

    List<XhwShowBar> findAllByOrderByLevelDesc();
}
