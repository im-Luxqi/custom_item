package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.enums.AwardRunningEnum;
import com.duomai.project.product.general.repository.XhwAwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/*对于抽中的实物奖品，完善地址信息
 * @description
 * @create by 王星齐
 * @time 2020-07-31 10:30:29
 **/
@Component
public class XhwRefreshAwardExecute implements IApiExecute {
    @Autowired
    private XhwAwardRepository xhwAwardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //当前参与抢购的奖品
        resultMap.put("hot_award", xhwAwardRepository.findFirstByAwardRunningTypeOrderByLevelDesc(AwardRunningEnum.RUNNING));
        return YunReturnValue.ok("完善地址成功");
    }
}
