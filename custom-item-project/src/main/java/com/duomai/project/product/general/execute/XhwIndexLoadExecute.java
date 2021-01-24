package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.XhwHelper;
import com.duomai.project.product.general.dto.XhwSettingDto;
import com.duomai.project.product.general.entity.*;
import com.duomai.project.product.general.enums.AwardRunningEnum;
import com.duomai.project.product.general.repository.XhwAwardRepository;
import com.duomai.project.product.general.repository.XhwPagePvLogRepository;
import com.duomai.project.product.general.repository.XhwShowBarRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 新华网首页load
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 */
@Component
public class XhwIndexLoadExecute implements IApiExecute {

    @Resource
    private XhwHelper xhwHelper;
    @Resource
    private XhwShowBarRepository xhwShowBarRepository;
    @Resource
    private XhwAwardRepository xhwAwardRepository;
    @Resource
    private XhwPagePvLogRepository xhwPagePvLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        String ip = sysParm.getApiParameter().getCommomParameter().getIp();
        //1.是否存在用户，不存在生成一个新用户
        XhwCustom custom = xhwHelper.findCustom(buyerNick, ip);


        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        XhwSettingDto xhwSetting = xhwHelper.findSetting();
        //活动说明
        resultMap.put("act_rule", xhwSetting.getActRule());
        //showbar
        List<XhwShowBar> newYearCard = xhwShowBarRepository.findAllByOrderByLevelDesc();
        if (CollectionUtils.isEmpty(newYearCard)) {
            newYearCard = new ArrayList<>();
        } else {
            newYearCard.forEach(x -> x.setLevel(null));
        }
        resultMap.put("new_year_card", newYearCard);

        //当前参与抢购的奖品
        XhwAward hotAward = xhwAwardRepository.findFirstByAwardRunningTypeOrderByLevelDesc(AwardRunningEnum.RUNNING);
        if (Objects.isNull(hotAward)) {
            hotAward = new XhwAward();
        } else {
            hotAward.setTotalNum(null);
            hotAward.setRemainNum(null);
            hotAward.setSendNum(null);
            hotAward.setLevel(null);
            hotAward.setAwardRunningType(null);
        }
        resultMap.put("hot_award", hotAward);
        //活动人数
        Integer joinNum = xhwHelper.findJoinNum();
        resultMap.put("player_num", xhwSetting.getVirtualNum() + joinNum);
        //中奖记录
        resultMap.put("draw_record", xhwHelper.drawLog());
        //群二维码
        resultMap.put("qr_code", custom.getGroupChat());

        resultMap.put("is_new_guy", custom.isNewGuy());
        resultMap.put("buyerNick", custom.getBuyerNick());


        XhwPagePvLog xhwPagePvLog = new XhwPagePvLog();
        xhwPagePvLog.setBuyerNick(buyerNick);
        xhwPagePvLog.setCreateTime(sysParm.getRequestStartTime());
        xhwPagePvLog.setIp(ip);
        xhwPagePvLogRepository.save(xhwPagePvLog);
        return YunReturnValue.ok(resultMap, "首页load," +
                "【提示：】" +
                "new_year_card  ---> 拜年贴" +
                "qr_code  ---> 入群二维码" +
                "is_new_guy ---> 新玩家 表示本机缓存中不存在buyernick"
        );
    }
}