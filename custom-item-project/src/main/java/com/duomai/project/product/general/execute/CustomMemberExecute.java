package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @内容：会员 入会状态
 * @创建人：wxq
 * @创建时间：2020.9.30
 */
@Component
public class CustomMemberExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        boolean historyMember = taobaoAPIService.isMember(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());

        Map result = new LinkedHashMap<>();
        result.put("have_join_member", historyMember);
        return YunReturnValue.ok(result,"完成签到任务！");
    }
}
