package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * //使用 letter_party3邀请函
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameUseLetterParty3Execute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        projectHelper.actTimeValidate();
        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Date requestStartTime = sysParm.getRequestStartTime();
        ActBaseSettingDto actSetting = projectHelper.actBaseSettingFind();
        Assert.isTrue(requestStartTime.after(actSetting.getActLastTime()), "12/24零点解锁");
        if ("party1,party2".equals(syscustom.getPlayParty()) && syscustom.getCurrentAction().equals(PlayActionEnum.letter_party3)) {
            syscustom.setPlayParty("party1,party2,party3");
            syscustom.setCurrentAction(PlayActionEnum.party3_ing);
        }
        sysCustomRepository.save(syscustom);
        return YunReturnValue.ok("使用letter_party3邀请函");
    }
}




