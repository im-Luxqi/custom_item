package com.duomai.project.configuration.annotation;


import java.lang.annotation.*;

/**
 * @description
 * @create by 王星齐
 * @time 2020-11-08 15:07:43
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JoinMemcache {


    /**
     * memcache中key值,默认：类名+方法名
     */
    String key() default "";
    /**
     * 刷新时间，按秒算,默认5秒
     * 不得小于2
     */
    int refreshTime() default 5;


}
