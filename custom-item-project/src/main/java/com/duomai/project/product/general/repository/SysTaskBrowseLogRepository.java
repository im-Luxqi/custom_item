package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskBrowseLog;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 王星齐
 */
public interface SysTaskBrowseLogRepository extends BaseRepository<SysTaskBrowseLog, String> {


    /**
     * 查询 玩家 记录
     *
     * @param buyerNick
     * @return
     */
    List<SysTaskBrowseLog> findByBuyerNick(String buyerNick);


    /**
     * 查询 玩家 指定哪天的 浏览记录
     *
     * @param buyerNick  玩家
     * @param browseTime 指定哪天  格式yyyy-MM-dd
     * @return
     */
    List<SysTaskBrowseLog> findByBuyerNickAndBrowseTime(String buyerNick, String browseTime);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
