package com.duomai.project.product.recycle;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import com.duomai.project.tool.LuckyDrawHelper;
import com.duomai.project.tool.ProjectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 抽奖接口
 * @description
 * @create by 王星齐
 * @time 2020-08-27 16:11:27
 **/
@Component
public class LuckyBagLoadExecute implements IApiExecute {
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*.数据展示*/
        Map result = new HashMap<>();
        //@1.我的奖品
        List<SysLuckyDrawRecord> byPlayerBuyerNickAndIsWin = sysLuckyDrawRecordRepository.findByPlayerBuyerNickAndIsWin(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick(), BooleanConstant.BOOLEAN_YES);
        byPlayerBuyerNickAndIsWin.forEach((x)->{
            x.setId(null);
            x.setLuckyChance(null);
            x.setPlayerBuyerNick(null);
            x.setIsWin(null);
        });
        result.put("my_lucky_bag", byPlayerBuyerNickAndIsWin);
        return YunReturnValue.ok(result, "玩家成功打开我的奖品");
    }
}
