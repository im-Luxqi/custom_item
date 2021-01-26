package com.duomai.project.api.taobao;

import com.duomai.project.configuration.SysCustomProperties;
import com.duomai.project.tool.ApplicationUtils;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.internal.OperationFuture;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MemcacheTools {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MemcacheTools.class);

    private static MemcachedClient memcachedClient = null;

    final static int one_secord = 1;

    static {
        SysCustomProperties sysCustomProperties = ApplicationUtils.getBean(SysCustomProperties.class);
        String username = sysCustomProperties.getOcsConfig().getUsername();
        String password = sysCustomProperties.getOcsConfig().getPassword();
        String host = sysCustomProperties.getOcsConfig().getHost();
        String port = sysCustomProperties.getOcsConfig().getPort();
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(username, password));
        try {
            memcachedClient = new MemcachedClient(
                    new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build(), AddrUtil.getAddresses(host + ":" + port));
        } catch (IOException e) {
            logger.error("ocs client error", e);
        }
    }


    public static boolean add(String key, int exp) {
        return MemcacheTools.add(key, 1, exp);
    }


    public static boolean add(String key) {
        return MemcacheTools.add(key, 1, one_secord);
    }


    /* add 操作
     * @description
     * @create by 王星齐
     * @time 2020-10-09 18:18:27
     * @param key
     * @param data
     * @param exp
     **/
    public static <T> boolean add(String key, T data, int exp) {
        OperationFuture<Boolean> aTrue = memcachedClient.add(key, exp, data);
        try {
            Boolean aBoolean = aTrue.get();
            String b = aBoolean ? "成功" : "失败";
//            logger.info("【Memcache】:" + "_操作:add——" + b + "_key：" + key + "_data:" + data);
            return aBoolean;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("【Memcache】:" + "_操作:add--异常" + "_key：" + key + "_data:" + data + "_error:" + e.getMessage());
        }
        return false;
    }


    /* 缓存数据
     * @description
     * @create by 王星齐
     * @time 2020-10-09 17:47:47
     * @param key
     * @param value
     * @param exp
     **/
    public static <T> void cacheData(String key, T data, int exp) {
        try {
            memcachedClient.set(key, exp, data);
//            logger.info("【Memcache】:" + "_操作:cacheData" + "_key：" + key + "_data:" + data);
        } catch (Exception e) {
            logger.error("【Memcache】:" + "_操作:cacheData" + "_key：" + key + "_data:" + data + "_error:" + e.getMessage());
        }
    }

    /* 加载缓存数据
     * @description
     * @create by 王星齐
     * @time 2020-10-09 17:47:59
     * @param key
     **/
    public static <T> T loadData(String key) {

        T object = null;
        try {
            object = (T) memcachedClient.get(key);
//            logger.info("【Memcache】:" + "_操作:loadData" + "_key：" + key + "_data:" + object);
        } catch (Exception e) {
            logger.error("【Memcache】:" + "_操作:loadData" + "_key：" + key + "_data:" + object + "_error:" + e.getMessage());
        }
        return object;
    }

    /* 清除缓存数据
     * @description
     * @create by 王星齐
     * @time 2020-10-09 17:48:11
     * @param key
     **/
    public static void cleanData(String key) {
        try {
            memcachedClient.delete(key);
//            logger.info("【Memcache】:" + "_操作:cleanData" + "_key：" + key);
        } catch (Exception e) {
            logger.error("【Memcache】:" + "_操作:cleanData" + "_key：" + key + "_error:" + e.getMessage());
        }
    }


    public static long addIncr(String key) {
        long incr = -1;
        try {
            incr = memcachedClient.incr(key, 1);
//            logger.info("【Memcache】:" + "_操作:cleanData" + "_key：" + key);
        } catch (Exception e) {
            logger.error("【Memcache】:" + "_操作:addIncr" + "_key：" + key + "_error:" + e.getMessage());
        }
        return incr;
    }


}
