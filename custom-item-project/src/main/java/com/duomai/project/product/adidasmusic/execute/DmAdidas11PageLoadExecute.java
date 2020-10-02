package com.duomai.project.product.adidasmusic.execute;


import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.OcsData;
import com.duomai.project.api.taobao.OcsUtil;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 活动load
 * @date 2020-10-02
 */
@Service
public class DmAdidas11PageLoadExecute implements IApiExecute {

    @Resource
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request
                                            , HttpServletResponse response) throws Exception {


        //校验活动是否在活动时间内
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);

        //取参
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Assert.hasLength(buyerNick, "粉丝昵称不能为空哦!");

        //预防并发
        projectHelper.checkoutMultipleCommit(sysParm,this::ApiExecute);

        //返回参数
        LinkedHashMap linkedHashMap = new LinkedHashMap();

        //活动基本信息
        linkedHashMap.put("actBaseSettingDto",actBaseSettingDto);

        //获取累计打卡次数



        return null;
    }
}
