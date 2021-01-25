package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwGroup;

import java.util.List;

public interface XhwGroupRepository extends BaseRepository<XhwGroup, String> {

    XhwGroup findFirstByFinish(int i);

    List<XhwGroup> findByTitleLike(String s);
}
