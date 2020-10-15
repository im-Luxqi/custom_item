package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*首页抽奖
 * @description
 * @create by 王星齐
 * @time 2020-07-31 10:30:29
 **/
@Component
public class IndexLuckyDrawExecute implements IApiExecute {
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysAwardRepository sysAwardRepository;
    @Autowired
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //防止连续点击
        projectHelper.checkoutMultipleCommit(sysParm, this);
        /*校验*/
        //是否在活动期间
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);
        //玩家是否存在
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        Assert.notNull(sysCustom, "不存在该玩家");
        boolean member = taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        Assert.isTrue(member, "亲，会员才可参与抽奖");
        List<SysAward> winAward = new ArrayList<>();
        long l = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndIsWinAndLuckyChanceIsNotNull(sysCustom.getBuyerNick(), BooleanConstant.BOOLEAN_YES);
        if (l == 0) {//首次发两张券
            List<SysAward> firstDraws = sysAwardRepository.findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum.FIRSTLUCKY);
            for (int i = 0; i < firstDraws.size(); i++) {
                List<SysAward> temp = new ArrayList<>();
                temp.add(firstDraws.get(i));
                SysAward w = luckyDrawHelper.luckyDraw(temp, sysCustom, sysParm.getRequestStartTime(), i != 0);
                if (w != null)
                    winAward.add(w);
            }
        } else {
            List<SysAward> thisTimeAwardPool = luckyDrawHelper.findCustomTimeAwardPool(sysCustom);
            SysAward w = luckyDrawHelper.luckyDraw(thisTimeAwardPool, sysCustom, sysParm.getRequestStartTime());
            if (w != null)
                winAward.add(w);
        }

        /*只反馈有效数据*/
        Map result = new HashMap<>();
        result.put("win", !CollectionUtils.isEmpty(winAward));
        result.put("awards", winAward);
        for (SysAward sysAward : winAward) {
            sysAward.setEname(null)
                    .setId(null)
                    .setRemainNum(null)
                    .setSendNum(null)
                    .setTotalNum(null)
                    .setLuckyValue(null)
                    .setPoolLevel(null);
        }
        return YunReturnValue.ok(result, "玩家成功进行抽奖");
    }
}
