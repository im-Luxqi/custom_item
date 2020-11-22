package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskMemberOrFollowLog;
import com.duomai.project.product.general.enums.TaskTypeEnum;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface SysTaskMemberOrFollowRepository extends BaseRepository<SysTaskMemberOrFollowLog, String> {


    /**
     * 查询 哪个玩家 做哪个任务 做了几次
     *
     * @param buyerNick
     * @param taskType
     * @return
     */
    long countByBuyerNickAndTaskType(String buyerNick, TaskTypeEnum taskType);


    //根据粉丝昵称、任务类型查询集合
    List<SysTaskMemberOrFollowLog> findByBuyerNickAndTaskType(String buyerNick, TaskTypeEnum taskType);


    //根据粉丝昵称、任务类型、时间段查找数据
    List<SysTaskMemberOrFollowLog> findByBuyerNickAndTaskTypeAndCreateTimeBetween(String buyerNick, TaskTypeEnum taskType,
                                                                                  Date start, Date end);

    /**
     * 查询 指定玩家  入会关注记录
     *
     * @param buyerNick
     * @return
     */
    List<SysTaskMemberOrFollowLog> findByBuyerNick(String buyerNick);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
