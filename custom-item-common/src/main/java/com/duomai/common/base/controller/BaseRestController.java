package com.duomai.common.base.controller;

import com.duomai.common.dto.YunReturnValue;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 基础校验
 *
 * @description
 */
public class BaseRestController {

    /* 参数检验
     * @description
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
