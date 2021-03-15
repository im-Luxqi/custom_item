package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 抽中的 奖品
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-09-29 14:52:42
 */
@Component
public class LuckyBagAllWinExecute implements IApiExecute {
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();

        /*.数据展示*/
        Map result = new HashMap<>();
        //@1.我的奖品
        List<SysLuckyDrawRecord> byPlayerBuyerNickAndIsWin = sysLuckyDrawRecordRepository.queryMybag(buyerNick);
        byPlayerBuyerNickAndIsWin.forEach((x)->{
            x.setLuckyChance(null);
            x.setPlayerBuyerNick(null);
            x.setIsWin(null);
//            x.setHaveExchange(null);
            x.setRemark(null);
            x.setPlayerZnick(null);
            x.setPlayerHeadImg(null);
            x.setDrawTime(null);
        });
        result.put("my_lucky_bag", byPlayerBuyerNickAndIsWin);
        return YunReturnValue.ok(result, "玩家成功打开我的奖品");
    }
}
