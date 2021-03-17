package com.duomai.project.helper;

import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;
import com.duomai.project.product.general.repository.SysTaskDailyBoardRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author cjw
 * @description 活动任务相关
 * @date 2020-10-02
 */
@Component
public class FinishTheTaskHelper {

    @Autowired
    private SysTaskDailyBoardRepository sysTaskDailyBoardRepository;

    /* 获取今日任务面板
     * @description
     * @create by 王星齐
     * @time 2021-03-12 19:12:37
     * @param buyerNick
     **/
    public SysTaskDailyBoard todayTaskBoard(String buyerNick) {
        if (ProjectTools.hasMemCacheEnvironment()) {
            Assert.isTrue(MemcacheTools.add("_todayTaskBoard_" + buyerNick), "点太快了，请休息下");
        }

        Date date = new Date();
        String todayString = CommonDateParseUtil.date2string(date, "yyyy-MM-dd");
        SysTaskDailyBoard taskDailyBoard = sysTaskDailyBoardRepository.findFirstByBuyerNickOrderByCreateTimeDesc(buyerNick);
        if (taskDailyBoard != null && todayString.equals(taskDailyBoard.getCreateTimeString())) {
            return taskDailyBoard;
        }

        String yestodayString = CommonDateParseUtil.date2string(CommonDateParseUtil.addDay(date, -1), "yyyy-MM-dd");
        Integer signTotalNum = taskDailyBoard != null ? taskDailyBoard.getSignTotalNum() : 0;
        Integer signContinuousNum = taskDailyBoard != null && yestodayString.equals(taskDailyBoard.getCreateTimeString()) ? taskDailyBoard.getSignContinuousNum() : 0;
        Integer haveFinishFollow = taskDailyBoard != null && taskDailyBoard.getHaveFinishFollow() == 1 ? 1 : 0;
        Integer haveFinishMember = taskDailyBoard != null && taskDailyBoard.getHaveFinishMember() == 1 ? 1 : 0;
        String inviteFollowProgress = taskDailyBoard != null ? taskDailyBoard.getInviteFollowProgress() : "(0/3)";
        SysTaskDailyBoard todayBoard = sysTaskDailyBoardRepository.save(new SysTaskDailyBoard()
                .setBuyerNick(buyerNick)
                .setCreateTime(date)
                .setCreateTimeString(todayString)
                .setSignTotalNum(signTotalNum)
                .setSignContinuousNum(signContinuousNum)
                .setHaveFinishFollow(haveFinishFollow)
                .setHaveFinishMember(haveFinishMember)
                .setInviteFollowProgress(inviteFollowProgress)
                .setHaveFinishBrowseToday(0)
                .setHaveFinishTvToday(0)
                .setTodayFinishShareNum("(0/2)")
                .setShareProgress("(0/5)")
                .setBrowseProgress("(0/3)")
                .setSpendProgress("(0/3)")
        );
        return todayBoard;
    }


    /*更新今日任务面板
     * @description
     * @create by 王星齐
     * @time 2021-03-12 19:17:37
     * @param taskDailyBoard
     **/
    @Transactional
    public void updateTaskBoard(SysTaskDailyBoard taskDailyBoard) {
        if (ProjectTools.hasMemCacheEnvironment()) {
            Assert.isTrue(MemcacheTools.add("_updateTaskBoard_" + taskDailyBoard.getBuyerNick()), "点太快了，请休息下");
        }
        taskDailyBoard.setUpdateTime(new Date());
        sysTaskDailyBoardRepository.save(taskDailyBoard);
    }

}
