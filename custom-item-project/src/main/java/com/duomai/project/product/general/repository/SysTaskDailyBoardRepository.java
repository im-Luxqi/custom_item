package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;

/**
 * @author 王星齐
 */
public interface SysTaskDailyBoardRepository extends BaseRepository<SysTaskDailyBoard, String> {

    /* 查询玩家 最近的一次任务面板记录
     * @description
     * @create by 王星齐
     * @time 2021-03-12 18:37:00
     * @param buyerNick
     **/
    SysTaskDailyBoard findFirstByBuyerNickOrderByCreateTimeDesc(String buyerNick);
}
