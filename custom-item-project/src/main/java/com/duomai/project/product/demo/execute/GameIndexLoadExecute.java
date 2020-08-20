package com.duomai.project.product.demo.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 游戏主页
 * @description
 * @create by 王星齐
 * @time 2020-07-29 10:24:46
 **/
@Component
public class GameIndexLoadExecute implements IApiExecute {

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
