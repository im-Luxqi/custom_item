package com.duomai.project.product.recycle.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.OcsUtil;
import com.duomai.project.product.general.dto.ActBaseSetting;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.tool.LuckyDrawHelper;
import com.duomai.project.tool.ProjectHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* 抽奖接口
 * @description
 * @create by 王星齐
 * @time 2020-08-27 16:11:27
 **/
@Slf4j
@Component
public class LuckyDrawExecute implements IApiExecute {
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private SysAwardRepository sysAwardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*防止重复提交*/
        if (!OcsUtil.add(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick() + "_lucky_draw_", "lucky", 2)){
            return YunReturnValue.fail("点太快了，请休息下");
        }
        log.info(OcsUtil.getObject(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick() + "_lucky_draw_").toString()+"-------------------------------");
        /*1.活动配置查询，活动期间才可访问接口*/
        ActBaseSetting actBaseSetting = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSetting);

        /*2.确认当前玩家身份*/
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        if (Objects.isNull(sysCustom)) {
            return YunReturnValue.fail("不存在该玩家");
        }

        /*3.抽奖*/
        List<SysAward> all = sysAwardRepository.findAllByOrderByLuckyValue();

        /*4.抽奖*/
        SysAward sysAward = luckyDrawHelper.luckyDraw(all, sysCustom, sysParm.getRequestStartTime());
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
