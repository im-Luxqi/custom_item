package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;

import java.util.List;

public interface SysCommodityRepository extends BaseRepository<SysCommodity, String> {

    //根据奖品类型及分类字段查询宝贝信息
    List<SysCommodity> queryByTypeAndCommoditySort(AwardTypeEnum type,String commoditySort);

}
