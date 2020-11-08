package com.duomai.project.api.gateway;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.enums.SysErrorEnum;
import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.product.adidasmusic.execute.*;
import com.duomai.project.product.general.execute.*;
import com.duomai.project.tool.ApplicationUtils;
import com.duomai.project.tool.ProjectTools;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class QLApiExecuteHandler {

    public static Map map = new HashMap<>();

    static {
        /*
         * 通用
         **/
        map.put("wx.dz.common.test", TestExecute.class); //测试阶段专用  wxq
        map.put("wx.dz.common.test.luckyChance", TestLuckyChanceExecute.class); //测试阶段专用 --增加指定玩家30次抽奖次数 wxq

        map.put("wx.dz.common.playerInfo.scan", PlayerInfoScanOrInitExecute.class);//玩家信息扫描，首次完成初始化操作 wxq
        map.put("wx.dz.common.playerInfo.fill.historyFollow", PlayerInfoFillForHistroyFollowExecute.class);//补全字段history_follow  wxq
        map.put("wx.dz.common.playerInfo.fill.afterAuthorization", PlayerInfoFillForAfterAuthorizationExecute.class);//授权成功后，完善用户信息 wxq

        map.put("wx.dz.common.luckyBag.allWin", LuckyBagAllWinExecute.class); //我的奖品 wxq
        map.put("wx.dz.common.luckyBag.fill.awardAddress", LuckyBagFillAwardAddressExecute.class); //留资料 wxq
//
//        map.put("wx.dz.common.pv", PagePvExecute.class);//pv  wxq


        /*
         * 阿迪2020双十一 定制
         **/
    /*    map.put("wx.dz.index.award.forinvite", IndexSendInviteAwardExecute.class); //首页发送邀请人员的奖励 wxq
        map.put("wx.dz.index.award.luckydraw", IndexLuckyDrawExecute.class); //抽奖 wxq

        map.put("wx.dz.task.finish.load", GeneralTaskLoadExecute.class); // 任务页面load:签到、关注是否完成 lyj
        map.put("wx.dz.task.sign.operate", GeneralTaskSignOperateExecute.class); // 任务页面：完成每日打卡 lyj
        map.put("wx.dz.task.follow.operate", GeneralTaskFollowOperateExecute.class); // 任务页面：完成关注店铺 lyj
        map.put("wx.dz.task.browse.operate", GeneralTaskBrowseOperateExecute.class); // 任务页面：完成浏览 lyj
        map.put("wx.dz.task.bigWheel.operate", GeneralTaskBigWheelOperateExecute.class); //任务页面：尖货大咖 lyj

        map.put("wx.dz.big.wheel.list", CusBigWheelListExecute.class); // 尖货大咖秀列表:活动标签 lyj
        map.put("wx.dz.page.load", DmAdidas11PageLoadExecute.class); //首页load cjw
        map.put("wx.dz.browseBaby.list.load", DmBrowseBabyListExecute.class); //浏览宝贝列表 cjw
        map.put("wx.dz.clickTo.browse", DmClickToBrowseExecute.class); //浏览宝贝送抽奖次数 cjw
        map.put("wx.dz.tools.browse.baby", DmBrowseBabySaveDelExecute.class); //封网备用 新增删除浏览宝贝接口 cjw
        map.put("wx.dz.tools.big.wheel", DmCusBigWheelSaveDelExecute.class); //封网备用 新增删除尖货大咖活动接口 cjw
        map.put("wx.dz.tools.sys.award", SysAwardSaveDelExecute.class); //封网备用 新增删除奖品接口 lyj
        map.put("wx.dz.tools.act.setting", DmModifyActivitySettingExecute.class); //封网备用 修改活动配置表信息 cjw

        map.put("wx.dz.membership.sys", DmMembershipExecute.class); // 判断当前粉丝是否入会 cjw
        map.put("wx.dz.invite.to.join", DmInviteToJoinExecute.class); // 邀请入会 cjw

        map.put("wx.dz.tools.get.order", CusGetOrderExecute.class); //拉取订单 wxq

//        map.put("wz.dz.tools.clear.log",DzToolsClearLogExecute.class); // 测试：清除记录

        map.put("wz.dz.tools.award.all", DzToolsAwardFindAllExecute.class); // 获得所有奖品*/


    }


    public static YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApplicationContext applicationContext = ApplicationUtils.getContext();
        IApiExecute sendApiExecute = null;
        for (Object o : map.keySet()) {
            if (o.equals(sysParm.getMethod())) {
                sendApiExecute = applicationContext.getBean((Class<IApiExecute>) map.get(o.toString()));
                break;
            }
        }
        if (sendApiExecute == null) {
            return YunReturnValue.fail(SysErrorEnum.VALID_EXECUTE);
        }

        //防连点
        if (ProjectTools.hasMemCacheEnvironment()) {
            Assert.isTrue(MemcacheTools.add("_checkoutMultipleCommit_" + sysParm.getApiParameter().getYunTokenParameter().getBuyerNick() + sendApiExecute.getClass().getName())
                    , "点太快了，请休息下");
        }
        return sendApiExecute.ApiExecute(sysParm, request, response);
    }
}
