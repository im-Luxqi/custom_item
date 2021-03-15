package com.duomai.project.product.general.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 卡牌获取通知
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-09-29 14:52:42
 */
@Component
public class LuckyNewChangeGetExecute implements IApiExecute {
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;

    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();

        /*.数据展示*/
        Map result = new LinkedHashMap();
        //@1.我的奖品
        List<SysLuckyChance> noShow = sysLuckyChanceRepository.findByBuyerNickAndHaveNotification(buyerNick, BooleanConstant.BOOLEAN_NO);

        if(!CollectionUtils.isEmpty(noShow)){
            noShow.forEach(x->{
                x.setHaveNotification(BooleanConstant.BOOLEAN_YES);
            });
            sysLuckyChanceRepository.saveAll(noShow);
            noShow.forEach(x->{
                x.setId(null);
                x.setGetTime(null);
                x.setUseTime(null);
                x.setBuyerNick(null);
                x.setIsUse(null);
                x.setTid(null);
                x.setTidTime(null);
                x.setHaveNotification(null);
            });
        }
        result.put("notification", noShow);
        return YunReturnValue.ok(result, "卡牌获取通知");
    }
}
