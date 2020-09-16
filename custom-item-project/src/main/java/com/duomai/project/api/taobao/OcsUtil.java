package com.duomai.project.api.taobao;

import com.duomai.common.util.MD5Utils;
import com.duomai.project.tool.ApplicationUtils;
import com.duomai.project.configuration.SysCustomProperties;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.internal.OperationFuture;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class OcsUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OcsUtil.class);

    private static MemcachedClient cache = null;

    static {
        SysCustomProperties sysCustomProperties = ApplicationUtils.getBean(SysCustomProperties.class);
        String username = sysCustomProperties.getOcsConfig().getUsername();
        String password = sysCustomProperties.getOcsConfig().getPassword();
        String host = sysCustomProperties.getOcsConfig().getHost();
        String port = sysCustomProperties.getOcsConfig().getPort();
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(username, password));
        try {
            cache = new MemcachedClient(
                    new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build(), AddrUtil.getAddresses(host + ":" + port));
        } catch (IOException e) {
            logger.error("ocs client error", e);
        }
    }

    public static boolean add(String key, OcsData data) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return true;
            String _key = MD5Utils.getMD5(key.getBytes());
            OperationFuture<Boolean> future = cache.add(_key, 2, data);
            try {
                return future.get();
            } catch (InterruptedException e) {
                logger.error("ocs add InterruptedException", e);
                return true;
            } catch (ExecutionException e) {
                logger.error("ocs add ExecutionException", e);
                return true;
            }
        }
        return false;
    }

    public static boolean add(String key, Object data, Integer time) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return true;
            String _key = MD5Utils.getMD5(key.getBytes());
            OperationFuture<Boolean> future = cache.add(_key, time, data);
            try {
                return future.get();
            } catch (InterruptedException e) {
                logger.error("ocs add InterruptedException", e);
                return true;
            } catch (ExecutionException e) {
                logger.error("ocs add ExecutionException", e);
                return true;
            }
        }
        return false;
    }

    public static boolean addInTwenty(String key, OcsData data) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return true;
            String _key = MD5Utils.getMD5(key.getBytes());
            OperationFuture<Boolean> future = cache.add(_key, 24, data);
            try {
                return future.get();
            } catch (InterruptedException e) {
                logger.error("ocs add InterruptedException", e);
                return true;
            } catch (ExecutionException e) {
                logger.error("ocs add ExecutionException", e);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取key的value值，不存在返回-1
     *
     * @param key
     * @return
     */
    public static Object getData(String key) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return null;
            String _key = MD5Utils.getMD5(key.getBytes());
            try {
                Object obj = cache.get(_key);
                logger.info("ocs get value" + obj);
                if (obj == null) {
                    return null;
                }
                return obj.toString();
            } catch (Exception e) {
                logger.error("ocs get Exception", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 获取key的value值，不存在返回-1
     *
     * @param key
     * @return
     */
    public static Integer get(String key) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return -1;
            String _key = MD5Utils.getMD5(key.getBytes());
            try {
                Object obj = cache.get(_key);
                logger.info("ocs get value" + obj);
                if (obj == null) {
                    return -1;
                }
                return Integer.parseInt(obj.toString());
            } catch (Exception e) {
                logger.error("ocs get Exception", e);
                return -1;
            }
        }
        return -1;
    }

    /**
     * 缓存自增
     *
     * @param key   （buyerNick+活动id）
     * @param value (value==null 默认自增加1 ; value !=null 新增，默认值为value）
     * @return 不存在返回-1 ，存在返回当前自增数
     */
    public static Integer incrAdd(String key, Integer value) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return -1;
            String _key = MD5Utils.getMD5(key.getBytes());
            try {
                if (value != null) {
                    Long count = cache.incr(_key, 0, value, 7200);
                    logger.info("ocs incrAdd count:" + count.toString());
                    return count.intValue();
                } else {
                    Long count = cache.incr(_key, 1);
                    logger.info("ocs incrAdd count:" + count.toString());
                    return count.intValue();
                }
            } catch (Exception e) {
                logger.error("ocs incrAdd Exception", e);
                return -1;
            }
        }
        return -1;
    }

    /**
     * 缓存自增
     *
     * @param key   （buyerNick+活动id）
     * @param value (value==null 默认自增加1 ; value !=null 新增，默认值为value）
     * @return 不存在返回-1 ，存在返回当前自增数
     */
    public static Integer incrAdd(String key, Integer value, Integer time) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return -1;
            String _key = MD5Utils.getMD5(key.getBytes());
            try {
                if (value != null) {
                    Long count = cache.incr(_key, 0, value, time);
                    logger.info("ocs incrAdd count:" + count.toString());
                    return count.intValue();
                } else {
                    Long count = cache.incr(_key, 1);
                    logger.info("ocs incrAdd count:" + count.toString());
                    return count.intValue();
                }
            } catch (Exception e) {
                logger.error("ocs incrAdd Exception", e);
                return -1;
            }
        }
        return -1;
    }


    public static boolean reallyDelete(String key) {
        if (StringUtils.isNotBlank(key)) {
            if (cache == null)
                return true;
            String _key = MD5Utils.getMD5(key.getBytes());
            OperationFuture<Boolean> future = cache.delete(_key);
            try {
                return future.get();
            } catch (InterruptedException e) {
                logger.error("ocs delete InterruptedException", e);
                return true;
            } catch (ExecutionException e) {
                logger.error("ocs delete ExecutionException", e);
                return true;
            }
        }
        return true;
    }
}
