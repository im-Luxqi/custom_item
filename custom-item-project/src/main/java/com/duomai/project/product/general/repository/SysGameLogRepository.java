package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysGameLog;
import com.duomai.project.product.general.enums.PlayPartnerEnum;
import org.springframework.transaction.annotation.Transactional;

public interface SysGameLogRepository extends BaseRepository<SysGameLog, String> {

    @Transactional
    void deleteByBuyerNick(String buyerNick);

    /**
     *
     * @param buyerNick
     * @param partnerEnum
     * @return
     */
    long countByBuyerNickAndPartner(String buyerNick, PlayPartnerEnum partnerEnum);
}
