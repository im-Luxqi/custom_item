package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysSettingCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;

import java.util.List;

public interface SysSettingCommodityRepository extends BaseRepository<SysSettingCommodity, String> {

    //根据奖品类型及分类字段查询宝贝信息
    List<SysSettingCommodity> queryByTypeAndCommoditySort(AwardTypeEnum type, String commoditySort);

    /**
     * 查询 商品
     * @param numId
     * @return
     */
    SysSettingCommodity findFirstByNumId(Long numId);
}