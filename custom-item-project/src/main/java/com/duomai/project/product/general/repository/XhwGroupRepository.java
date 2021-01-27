package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwGroup;

import java.util.List;

public interface XhwGroupRepository extends BaseRepository<XhwGroup, String> {
    List<XhwGroup> findByTitleLike(String s);
    List<XhwGroup> findByFinish(int i);
}
