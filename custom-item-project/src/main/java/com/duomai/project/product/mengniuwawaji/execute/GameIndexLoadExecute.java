package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 游戏首页 加载
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexLoadExecute implements IApiExecute {

    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;

    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;

    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;


    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;


    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp()).setPage(PvPageEnum.PAGE_INDEX)
        );

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //1.活动规则
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        actBaseSettingDto.setActLastTime(null);
        actBaseSettingDto.setOrderStartTime(null);
        actBaseSettingDto.setOrderEndTime(null);
        resultMap.put("game_rule", actBaseSettingDto);
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        //2.兑换弹幕
        resultMap.put("lucky_barrage", luckyDrawHelper.luckyBarrage());
        return YunReturnValue.ok(resultMap, "游戏门头");
    }
}




