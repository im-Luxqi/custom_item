package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskBrowseLog;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 王星齐
 */
public interface SysTaskBrowseLogRepository extends BaseRepository<SysTaskBrowseLog, String> {

    //查询该商品在时间内是否被浏览过
    SysTaskBrowseLog findFirstByBuyerNickAndCreateTimeBetweenAndNumId(String buyerNick, Date start, Date end, Long numId);

    //查询该粉丝在时间内浏览的日志
    List<SysTaskBrowseLog> findByBuyerNickAndCreateTimeBetween(String buyerNick, Date start, Date end);

    // 根据昵称
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
