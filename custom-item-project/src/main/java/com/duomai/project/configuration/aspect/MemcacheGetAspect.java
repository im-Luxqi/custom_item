package com.duomai.project.configuration.aspect;

import com.duomai.project.api.taobao.MemCacheData;
import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.tool.ApplicationUtils;
import com.duomai.project.tool.ProjectTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 从memcache中获取想要的
 *
 * @description
 * @create by 王星齐
 * @time 2020-11-08 16:51:50
 */
@Aspect
@Component
@Slf4j
public class MemcacheGetAspect {
    final static String lock = "_lock_";
    final static int lock_time = 100;
    final static int wait_time = 50;
    final static int delayed_time = 1;

    @Pointcut("@annotation(com.duomai.project.configuration.annotation.JoinMemcache)")
    public void memcacheGet() {
    }

    @Around("memcacheGet()")
    public Object deAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        JoinMemcache join = method.getAnnotation(JoinMemcache.class);
        String className = point.getTarget().getClass().getName();
        String property = ApplicationUtils.getContext().getEnvironment().getProperty("spring.profiles.active");
        String key = property + (StringUtils.isNotBlank(join.key()) ? join.key() : (className + method.getName()));
        String lock_key = property + lock + key;
        int timeout = join.refreshTime();

        if (!ProjectTools.hasMemCacheEnvironment()) {
            return point.proceed();
        }
        boolean endFlag = false;
        MemCacheData memCacheData = null;
        while (!endFlag) {
            memCacheData = MemcacheTools.loadData(key);
            if (Objects.isNull(memCacheData)) {
                if (MemcacheTools.add(lock_key, lock_time)) {
                    memCacheData = new MemCacheData<>(timeout - delayed_time).setData(point.proceed());
                    MemcacheTools.cacheData(key, memCacheData, timeout);
                    MemcacheTools.cleanData(lock_key);
                } else {
                    Thread.sleep(wait_time);
                }
            } else if (memCacheData.getTimeout() <= System.currentTimeMillis()) {
                if (MemcacheTools.add(lock_key, lock_time)) {
                    MemcacheTools.cacheData(key, new MemCacheData<>(timeout - delayed_time).setData(memCacheData.getData()), timeout);
                    memCacheData = new MemCacheData<>(timeout - delayed_time).setData(point.proceed());
                    MemcacheTools.cacheData(key, memCacheData, timeout);
                    MemcacheTools.cleanData(lock_key);
                } else {
                    Thread.sleep(wait_time);
                }
            }
            if (!Objects.isNull(memCacheData))
                endFlag = true;
        }
        return memCacheData.getData();
    }
}
