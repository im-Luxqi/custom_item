package com.duomai.project.api.gateway;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.enums.SysErrorEnum;
import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.product.general.execute.*;
import com.duomai.project.product.mengniuwawaji.execute.*;
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
        /**
         * 通用
         */
        //测试阶段专用  wxq
//        map.put("wx.dz.common.test", TestExecute.class);
        //测试阶段专用 --增加指定玩家30次抽奖次数 wxq
//        map.put("wx.dz.common.test.luckyChance", TestLuckyChanceExecute.class);

        //测试阶段专用 --kill wxq
        map.put("wx.dz.common.test.kill", TestKillChanceExecute.class);

        //1.玩家信息扫描，首次完成初始化操作 wxq
        map.put("wx.dz.common.playerInfo.scan", PlayerInfoScanOrInitExecute.class);
        //授权成功后，完善用户信息 wxq
        map.put("wx.dz.common.playerInfo.fill.afterAuthorization", PlayerInfoFillForAfterAuthorizationExecute.class);

        //2 落地页，门头页load
        map.put("wx.dz.game.index.load", GameIndexLoadExecute.class);


        //party1  场景load
        map.put("wx.dz.game.party1.load", GameIndexParty1Execute.class);


        //2.1 场景1 和雪人玩
        map.put("wx.dz.game.party1.play.snowman", GamePlaySnowmanExecute.class);
        //2.2 场景1 和企鹅玩
        map.put("wx.dz.game.party1.play.penguin", GamePlayPenguinExecute.class);
        //2.3  开礼盒
        map.put("wx.dz.game.party.open.luckyBox", GameOpenLuckyBoxExecute.class);
        //2.7  分享
        map.put("wx.dz.game.party.share", GameShareExecute.class);
        //2.4  白熊 获取三道新题目
        map.put("wx.dz.game.party1.bear.threeQuestion", GameBearGetThreeQuestionExecute.class);
        //2.6  白熊 答题
        map.put("wx.dz.game.party1.bear.answer", GameBearAnswerExecute.class);
        //2.8  使用 letter_party2邀请函
        map.put("wx.dz.game.party1.use.letterParty2", GameUseLetterParty2Execute.class);
        //party2 load
        map.put("wx.dz.game.party2.load", GameIndexParty2Execute.class);
        //2.9  场景2 点灯
        map.put("wx.dz.game.party2.play.lamp", GamePlayLampExecute.class);
        //3.0  狗 浏览商品列表
        map.put("wx.dz.game.party2.play.dog.goodsList", ShowBrowseListExecute.class);
        //3.1  狗 浏览
        map.put("wx.dz.game.party2.play.dog.browse", TaskBrowseExecute.class);


        //2.8  使用 letter_party3邀请函
        map.put("wx.dz.game.party2.use.letterParty3", GameUseLetterParty3Execute.class);


        //3.2  场景2 和气球玩
        map.put("wx.dz.game.party2.play.balloon", GamePlayBalloonExecute.class);

        //我的奖品 wxq
        map.put("wx.dz.common.luckyBag.win", LuckyBagAllWinExecute.class);
        //留资料 wxq
        map.put("wx.dz.common.luckyBag.win.address", LuckyBagFillAwardAddressExecute.class);
        //入会任务

        //任务--成为会员
        map.put("wx.dz.game.task.member", TaskMemberExecute.class);

        //3.游戏首页 小彩蛋
        map.put("wx.dz.game.index.egg", GameIndexEggExecute.class);


        //2.游戏首页 加载
//        map.put("wx.dz.game.index.load", GameIndexLoadExecute.class);


        //1.进入场景1
//        map.put("wx.dz.go.snowman", PlayerInfoScanOrInitExecute.class);
        //1.为雪人带上节日围巾
//        map.put("wx.dz.play.snowman", PlayerInfoScanOrInitExecute.class);


        //补全字段history_follow  wxq
//        map.put("wx.dz.common.playerInfo.fill.historyFollow", PlayerInfoFillForHistroyFollowExecute.class);

        //玩家入会状态 wxq
//        map.put("wx.dz.common.playerInfo.member.state", CustomMemberExecute.class);

        /**
         * 首页
         */
        //游戏首页 加载
//        map.put("wx.dz.game.index.load", GameIndexLoadExecute.class);
        //抽奖机会通知
//        map.put("wx.dz.game.index.notification", LuckyNewChangeGetExecute.class);
        //点击抓娃娃
//        map.put("wx.dz.game.lucky.draw", GameIndexLuckyDrawExecute.class);


        //我的战利品-查看明细
//        map.put("wx.dz.game.show.list.exchange", ShowExchangeListExecute.class);

        //立即兑换
//        map.put("wx.dz.game.lucky.exchange", GameIndexLuckyExchangeExecute.class);

        //本机奖品池
//        map.put("wx.dz.game.lucky.pool", GameIndexLuckyPoolExecute.class);


        /**
         * 任务
         */
        //任务页面load
//        map.put("wx.dz.game.task.load", TaskDashboardExecute.class);


        //任务--每日签到
//        map.put("wx.dz.game.task.sign", TaskSignExecute.class);

        //任务--成为会员
//        map.put("wx.dz.game.task.member", TaskMemberExecute.class);

        //任务--关注店铺
//        map.put("wx.dz.game.task.follow", TaskFollowExecute.class);


        //任务--浏览商品
//        map.put("wx.dz.game.task.browse", TaskBrowseExecute.class);


        //任务--观看直播
//        map.put("wx.dz.game.task.tv", TaskTvExecute.class);


        //任务--消费增送
//        map.put("wx.dz.game.task.order", CusGetOrderExecute.class);

        //任务--浏览页
//        map.put("wx.dz.game.show.list.browse", ShowBrowseListExecute.class);


        //任务--邀请明细
//        map.put("wx.dz.game.show.list.invite", ShowInviteListExecute.class);


        //任务--分享明细
//        map.put("wx.dz.game.show.list.share", ShowShareListExecute.class);


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
            Assert.isTrue(MemcacheTools.add("_checkoutMultipleCommit_"
                    + sysParm.getApiParameter().getYunTokenParameter().getBuyerNick()
                    + sendApiExecute.getClass().getName()), "点太快了，请休息下");
        }
        return sendApiExecute.ApiExecute(sysParm, request, response);
    }
}
