package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //防止连续点击
        projectHelper.checkoutMultipleCommit(sysParm,this);
        /*校验*/
        //是否在活动期间
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);
        //玩家是否存在
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        Assert.notNull(sysCustom, "不存在该玩家");


        List<SysAward> thisTimeAwardPool = luckyDrawHelper.findCustomTimeAwardPool(sysCustom);
        SysAward sysAward = luckyDrawHelper.luckyDraw(thisTimeAwardPool, sysCustom, sysParm.getRequestStartTime());
        /*只反馈有效数据*/
        Map result = new HashMap<>();
        result.put("win", sysAward != null);
        result.put("award", sysAward);
        if (sysAward != null) {
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
