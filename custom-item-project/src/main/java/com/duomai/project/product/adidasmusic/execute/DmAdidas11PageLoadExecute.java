package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.util.CommonHanZiUtil;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.CommonExceptionEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 活动load
 * @time 2020-10-02
 */
@Service
public class DmAdidas11PageLoadExecute implements IApiExecute {

    @Resource
    private ProjectHelper projectHelper;
    @Resource
    private LuckyDrawHelper drawHelper;
    @Resource
    private SysCustomRepository customRepository;
    @Resource
    private SysInviteLogRepository inviteLogRepository;
    @Resource
    private SysLuckyDrawRecordRepository drawRecordRepository;
    @Resource
    private SysAwardRepository awardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        /*预防并发，校验活动是否在活动时间内*/
        projectHelper.checkoutMultipleCommit(sysParm, this);
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);

        /*初始化新粉丝，粉丝每日首次登陆赠送一次抽奖机会*/
        SysCustom sysCustom = customRepository.findByBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        if (sysCustom == null) {
            sysCustom = customRepository.save(projectHelper.customInit(sysParm));
            drawHelper.sendLuckyChance(sysCustom.getBuyerNick(), LuckyChanceFromEnum.FIRST, 1);
        } else {
            long l = drawHelper.countTodayLuckyChanceFrom(sysCustom.getBuyerNick(), LuckyChanceFromEnum.FIRST);
            if (l == 0) {
                drawHelper.sendLuckyChance(sysCustom.getBuyerNick(), LuckyChanceFromEnum.FIRST, 1);
            }
        }

        //获取邀请奖品信息
        SysAward awardInvite = awardRepository.queryFirstByUseWay(AwardUseWayEnum.INVITE);
        Assert.notNull(awardInvite, "未获取到邀请奖品信息!");
        //获取该粉丝是否已获得日志
        SysLuckyDrawRecord drawRecord = drawRecordRepository.findFirstByPlayerBuyerNickAndAwardId(sysCustom.getBuyerNick(),awardInvite.getId());
        if (drawRecord != null) {
            awardInvite.setLogId(drawRecord.getId());
        }else{
            awardInvite.setLogId("");
        }

        //返回参数
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("signDto", drawHelper.findCurrentPoolLevel(sysCustom));
        //活动基本信息
        linkedHashMap.put("actBaseSettingDto", actBaseSettingDto);
        //粉丝信息
        linkedHashMap.put("sysCustom", sysCustom);
        //邀请日志记录
        linkedHashMap.put("inviteLogs", inviteLogRepository.findByInviter(sysCustom.getBuyerNick()));
        //邀请奖品信息
        linkedHashMap.put("awardInvite", awardInvite);
        //获取目前剩余抽奖次数
        linkedHashMap.put("drawNum", drawHelper.unUseLuckyChance(sysCustom.getBuyerNick()));
        //获取当前粉丝奖池等级
        linkedHashMap.put("signDto", drawHelper.findCurrentPoolLevel(sysCustom));

        //中奖弹幕 展示50条
        List luckyDrawRecords = drawRecordRepository.queryLuckyDrawLog();
        linkedHashMap.put("luckyDrawRecords", luckyDrawRecords.size() > 50 ? luckyDrawRecords : getFakeData());

        return YunReturnValue.ok(linkedHashMap, CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }


    static List<SysLuckyDrawRecord> fakeData;

    static List<SysLuckyDrawRecord> getFakeData() {
        if (fakeData == null) {
            List<SysLuckyDrawRecord> objects = new ArrayList<>(50);
            for (int i = 0; i < 50; i++) {
                objects.add(new SysLuckyDrawRecord()
                        .setAwardName("满减优惠券")
                        .setPlayerBuyerNick(CommonHanZiUtil.randomGetUnicodeHanZi() + "***"));
            }
            fakeData = objects;
        }
        return fakeData;
    }
}
