package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.repository.SysAwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 记录pv
 * @author im-luxqi
 * @description (哪个渠道，页面)
 * @create by 王星齐
 * @time 2020-08-26 19:11:50
 */
@Component
public class TestExecute implements IApiExecute {
    @Autowired
    private SysAwardRepository sysAwardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        sysAwardRepository.findByUseWayAndPoolLevelLessThanEqualOrderByLuckyValueAsc(AwardUseWayEnum.POOL, 5);
        return YunReturnValue.ok("");
    }
}
