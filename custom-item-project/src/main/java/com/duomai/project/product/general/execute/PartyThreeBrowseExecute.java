package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameLog;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.enums.PlayPartnerEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @内容：任务页面 场景3浏览操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class PartyThreeBrowseExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysTaskBrowseLogRepository sysTaskBrowseLogRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;
    @Autowired
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;
    @Autowired
    private SysGameLogRepository sysGameLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验*/
        //活动只能再活动期间
        projectHelper.actTimeValidate();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        Assert.isTrue(syscustom.getCurrentAction().equals(PlayActionEnum.party3_ing), "party3_ing才可用");


        //4.记录互动日志
        sysGameLogRepository.save(new SysGameLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(requestStartTime)
                .setCreateTimeString(requestStartTimeString)
                .setPartner(PlayPartnerEnum.partybrowse));

        long l = sysGameLogRepository.countByBuyerNickAndPartner(buyerNick, PlayPartnerEnum.partybrowse);
        if (l <= CoachConstant.browse_limit_count_last) {
            syscustom.setHaveBrowseGoods(BooleanConstant.BOOLEAN_YES);
            syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.browse_xingyuan_last);
            syscustom = sysCustomRepository.save(syscustom);
        }


        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap, "场景3浏览操作");
    }
}
