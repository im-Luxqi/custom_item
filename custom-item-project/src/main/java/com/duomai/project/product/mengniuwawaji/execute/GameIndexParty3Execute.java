package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActTreeWinDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.product.general.repository.SysPagePvLogRepository;
import com.duomai.project.product.general.repository.SysTaskShareLogRepository;
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
public class GameIndexParty3Execute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {



        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Date requestStartTime = sysParm.getRequestStartTime();
        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp())
                .setPage(PvPageEnum.PAGE_PARTY3));

        long hasGet = sysLuckyDrawRecordRepository.countByPlayerBuyerNickAndLuckyChance(buyerNick, "_ranging");
        ActTreeWinDto actTreeWinDto = projectHelper.treeWinSettingFind();
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //1.表示玩家是否点亮过圣诞树
        resultMap.put("have_light_tree", hasGet > 0);
        resultMap.put("can_light_tree", requestStartTime.after(actTreeWinDto.getTimeTreeLimit()));
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        return YunReturnValue.ok(resultMap, "场景3" +
                "have_light_tree = true ---> 表示玩家是否点亮过圣诞树");
    }
}




