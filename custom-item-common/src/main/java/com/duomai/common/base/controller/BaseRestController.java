package com.duomai.common.base.controller;

import com.duomai.common.dto.YunReturnValue;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description
 * @create by 王星齐
 * @date 2019-10-17 20:12
 */
public class BaseRestController {

    /* 参数检验
     * @description
     * @create by 王星齐
     * @time 2019-10-18 14:11:23
     * @param ex
     **/
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    protected YunReturnValue paramNotValidHandle(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException)
            return YunReturnValue.fail(
                    ((MethodArgumentNotValidException) ex).getBindingResult()
                            .getAllErrors()
                            .get(0)
                            .getDefaultMessage());
        return YunReturnValue.fail(
                ((BindException) ex).getBindingResult()
                        .getAllErrors()
                        .get(0)
                        .getDefaultMessage());
    }
}
