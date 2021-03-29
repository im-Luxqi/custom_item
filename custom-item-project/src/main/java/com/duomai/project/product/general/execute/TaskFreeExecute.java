package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * @内容：任务页面 首次免费赠送
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class TaskFreeExecute implements IApiExecute {
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        Boolean flag = false;
        if (projectHelper.actTimeValidateFlag()) {
//                首次登录游戏免费送一次
            long l = luckyDrawHelper.countLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.FREE);
            if (l == 0) {
                flag = true;
                int getNum = 1;
                luckyDrawHelper.sendCard(buyerNick, LuckyChanceFromEnum.FREE, getNum,
                        "恭喜你！");

            }
        }
        LinkedHashMap<Object, Object> objectObjectLinkedHashMap = new LinkedHashMap<>();
        objectObjectLinkedHashMap.put("is_first", flag);
        return YunReturnValue.ok(objectObjectLinkedHashMap, "首次登录游戏免费送一次");
    }
}
