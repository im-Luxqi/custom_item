package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.mengniuwawaji.domain.CusOrderInfo;
import com.duomai.project.product.mengniuwawaji.service.ICusOrderInfoService;
import com.taobao.api.response.OpenTradesSoldGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 游戏首页 小彩蛋
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexEggExecute implements IApiExecute {

    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private ProjectHelper projectHelper;

    @Resource
    private ICusOrderInfoService cusOrderInfoService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //2.是否浏览过商品
        resultMap.put("have_browse", BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveBrowseGoods()));
        //1.是否邀请过好友
        resultMap.put("have_invite", BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveInviteFriend()));
        //3.是否购买过coach商品
        resultMap.put("have_spend", BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveSpendGoods()));
        //4.是否加入会员
        resultMap.put("have_member", taobaoAPIService.isMember(buyerNick));
        return YunReturnValue.ok(resultMap, "小彩蛋");
    }
}




