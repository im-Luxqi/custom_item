package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
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

/**
 * 看直播
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-05-09 09:13:09
 */
@Component
public class TaskTvExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验*/
        //活动只能再活动期间
        projectHelper.actTimeValidate();
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");

        long tvCount = luckyDrawHelper.countTodayLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.TV);
        if (tvCount == 0) {
            luckyDrawHelper.sendLuckyChance(buyerNick, LuckyChanceFromEnum.TV, 2,
                    "直播", "今日观看直播，获得" + 2 + "次游戏机会");
        }
        return YunReturnValue.ok("直播");
    }
}
