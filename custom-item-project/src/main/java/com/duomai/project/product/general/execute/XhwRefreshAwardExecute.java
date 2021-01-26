package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.XhwHelper;
import com.duomai.project.product.general.dto.XhwSettingDto;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.enums.AwardRunningEnum;
import com.duomai.project.product.general.repository.XhwAwardRecordRepository;
import com.duomai.project.product.general.repository.XhwAwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Objects;

/*对于抽中的实物奖品，完善地址信息
 * @description
 * @create by 王星齐
 * @time 2020-07-31 10:30:29
 **/
@Component
public class XhwRefreshAwardExecute implements IApiExecute {
    @Autowired
    private XhwAwardRepository xhwAwardRepository;
    @Autowired
    private XhwAwardRecordRepository xhwAwardRecordRepository;
    @Autowired
    private XhwHelper xhwHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //当前参与抢购的奖品
        XhwAward hotAward = xhwAwardRepository.findFirstByAwardRunningTypeOrderByLevelDesc(AwardRunningEnum.RUNNING);
        if (Objects.isNull(hotAward)) {
            hotAward = xhwAwardRepository.findFirstByAwardRunningTypeOrderByLevelAsc(AwardRunningEnum.FINISH);
            hotAward.setTotalNum(null);
            hotAward.setRemainNum(null);
            hotAward.setSendNum(null);
            hotAward.setLevel(null);
        } else {
            long l = xhwAwardRecordRepository.countByBuyerNickAndAwardId(buyerNick, hotAward.getId());
            hotAward.setHasGet(l > 0);
            hotAward.setTotalNum(null);
            hotAward.setRemainNum(null);
            hotAward.setSendNum(null);
            hotAward.setLevel(null);
        }
        resultMap.put("hot_award", hotAward);
        //活动人数
        XhwSettingDto xhwSetting = xhwHelper.findSetting();
        Integer joinNum = xhwHelper.findJoinNum();
        resultMap.put("player_num", xhwSetting.getVirtualNum() + joinNum);
        //中奖记录

        resultMap.put("draw_record", xhwHelper.drawLog(hotAward));
        return YunReturnValue.ok(resultMap, "当前参与抢购的奖品");
    }
}
