package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @内容：任务页面 尖货大咖操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 * */
@Component
public class GeneralTaskBigWheelOperateExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //获取参数
        JSONObject object = JSONObject.parseObject(sysParm.getApiParameter().getAdmjson().toString());
        String gateway = object.getString("gateway");
        Assert.notNull(gateway, "入口不能为空");

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        // 校验玩家是否存在
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(sysCustom, "不存在该玩家");

        return YunReturnValue.ok("操作成功");
    }
}
