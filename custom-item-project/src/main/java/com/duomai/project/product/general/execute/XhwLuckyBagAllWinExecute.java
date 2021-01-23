package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.XhwHelper;
import com.duomai.project.product.general.entity.XhwAwardRecord;
import com.duomai.project.product.general.entity.XhwCustom;
import com.duomai.project.product.general.repository.XhwAwardRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽中的 奖品
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-09-29 14:52:42
 */
@Component
public class XhwLuckyBagAllWinExecute implements IApiExecute {
    @Autowired
    private XhwAwardRecordRepository xhwAwardRecordRepository;
    @Autowired
    private XhwHelper xhwHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();

        XhwCustom custom = xhwHelper.findCustom(buyerNick);
        List<XhwAwardRecord> xhwAwardRecordList = xhwAwardRecordRepository.findByBuyerNickOrderByDrawTimeDesc();

        /*.数据展示*/
        Map result = new HashMap<>();
        //@1.我的奖品
        xhwAwardRecordList.forEach((x) -> {
            x.setBuyerNick(null);
            x.setIp(null);
            x.setId(null);
            x.setDrawTime(null);
            x.setDrawTimeString(null);
            x.setReceviceTime(null);
            x.setReceviceTimeString(null);
            x.setReceviceAddress(null);
            x.setReceviceDistrict(null);
            x.setReceviceCity(null);
            x.setReceviceProvince(null);
        });
        result.put("my_lucky_bag", xhwAwardRecordList);
        return YunReturnValue.ok(result, "玩家成功打开我的奖品");
    }
}
