package com.duomai.new_custom_base.api.product.core.service;


import com.duomai.new_custom_base.api.product.core.apiReturnValue.YunReturnValue;
import com.duomai.new_custom_base.api.product.core.pojo.ApiSysParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IApiExecute {

    /**
     * 具体实现接口的业务需要继续并实现的方法
     *
     * @param sysParm API接收到的参数对象，其中业务级的参数是json格式的字符串，需要各业务类进行jsonToObject操作
     */
    YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
