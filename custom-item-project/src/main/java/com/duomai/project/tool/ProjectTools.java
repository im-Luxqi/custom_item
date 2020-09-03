package com.duomai.project.tool;

import com.duomai.common.base.enums.IEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 项目中 实用的工具
 *
 * @description
 * @create by 王星齐
 * @date 2020-08-28 11:39
 */
public class ProjectTools {

    /*  转化枚举
     * @description
     * @create by 王星齐
     * @time 2020-08-28 11:45:18
     * @param clazz
     * @param code
     **/
    public static <T extends IEnum> T enumValueOf(Class<T> clazz, String code) {
        Assert.isTrue(clazz.isEnum(), clazz + "不是枚举类型");
        if (StringUtils.isBlank(code)) {
            return null;
        }
        T[] enums = clazz.getEnumConstants();
        if (enums == null || enums.length == 0) {
            return null;
        }
        for (T t : enums) {
            if (t.getValue().equals(code)) {
                return t;
            }
        }
        return null;
    }

    /*参数校验
     * @description
     * @create by 王星齐
     * @time 2020-08-28 11:56:54
     * @param obj
     **/
    public static void validateParam(Object obj) throws Exception {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0)
            throw new Exception("必传参数校验，" + constraintViolations.iterator().next().getMessage());
    }

}
