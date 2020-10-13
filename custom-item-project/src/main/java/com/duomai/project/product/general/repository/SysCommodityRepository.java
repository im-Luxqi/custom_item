package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;

import java.util.List;

public interface SysCommodityRepository extends BaseRepository<SysCommodity, String> {

    List<SysCommodity> queryAllByTypeAndAndCommoditySort(AwardTypeEnum type,String commoditySort);

}
