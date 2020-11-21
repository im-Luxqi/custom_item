package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysSettingCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;

import java.util.List;

/**
 * @author im-luxqi
 */
public interface SysSettingCommodityRepository extends BaseRepository<SysSettingCommodity, String> {

    /**
     * 查询 商品
     * @param numId
     * @return
     */
    SysSettingCommodity findFirstByNumId(Long numId);
}
