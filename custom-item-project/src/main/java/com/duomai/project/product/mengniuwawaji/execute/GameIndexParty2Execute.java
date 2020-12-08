package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysGameBoardDailyRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysPagePvLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 场景2 load
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexParty2Execute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {



        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp())
                .setPage(PvPageEnum.PAGE_PARTY2));


        ActBaseSettingDto actSetting = projectHelper.actBaseSettingFind();

        long l = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChance(buyerNick, AwardUseWayEnum.PARTY2.getValue());
        long l2 = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChance(buyerNick, AwardUseWayEnum.TENT.getValue());
        SysGameBoardDaily daily = sysGameBoardDailyRepository.findFirstByBuyerNickAndCreateTimeString(buyerNick, requestStartTimeString);


        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //1.是否开启过礼盒
        resultMap.put("have_open_award_party2", l > 0);
        resultMap.put("have_open_award_tent", l2 > 0);
//        resultMap.put("get_letter_party2", syscustom.getCurrentAction().equals(PlayActionEnum.letter_party2));
//        resultMap.put("first_play_Tent", daily.getFirstGameTent().equals(BooleanConstant.BOOLEAN_YES));
        resultMap.put("first_play_Lamp", daily.getFirstGameLamp().equals(BooleanConstant.BOOLEAN_YES));
        resultMap.put("first_play_Dog", daily.getFirstGameDog().equals(BooleanConstant.BOOLEAN_YES));



        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap, "场景2" +
                "\nhave_open_award_party2 = true ---> 表示玩家已经开过场景2开场礼盒" +
                "\nhave_open_award_tent = true ---> 表示玩家已经开过帐篷礼盒"
        );
    }
}




