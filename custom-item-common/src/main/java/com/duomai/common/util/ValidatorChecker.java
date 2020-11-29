package com.duomai.common.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 参数校验器
 *
 * @description
 * @create by 王星齐
 */
public class ValidatorChecker {
    public static void checkParam(Object obj) throws Exception {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0)
            throw new Exception(constraintViolations.iterator().next().getMessage());
//            throw new Exception("必传参数校验，" + constraintViolations.iterator().next().getMessage());
    }
}
