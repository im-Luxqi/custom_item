package com.duomai.project.api.gateway.tool;


import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.util.EncryptUtil;
import com.duomai.common.util.MD5Utils;
import com.duomai.project.tool.ApplicationUtils;
import com.duomai.starter.SysProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
public class ApiTool {

    /**
     * <p>
     * 获取客户端的IP地址的方法是：requestBody.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了，如果通过了多级反向代理的话，
     * X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120,
     * 192.168.1.130, 192.168.1.100 用户真实IP为： 192.168.1.110
     * </p>
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        // nginx代理获取的真实用户ip
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        /*
          对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() =
          15
         */
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public static boolean signCheck(ApiSysParameter sysParm) {
        /**
         * 获取参数
         */
        String method = sysParm.getMethod();
        String appkey = sysParm.getApiParameter().getCommomParameter().getAppkey();
        String timestamp = sysParm.getApiParameter().getCommomParameter().getTimestamp();
        String frontSign = sysParm.getApiParameter().getCommomParameter().getSign();
        String bizParam = sysParm.getApiParameter().getCommomParameter().getAdmjsonStr();

        /**
         *  校验参数是否存在
         */
        String secretKey = ApplicationUtils.getBean(SysProperties.class).getSysConfig().getSecretkey();
        String behindSign;
        try {
            String zhString = secretKey + "admjson" +
                    EncryptUtil.adjustURLEncoderForJsEncodeURIComponent(bizParam)
                    + "appkey" + appkey + "m" + method + "timestamp" + timestamp + secretKey;
            behindSign = MD5Utils.getMD5(zhString.toLowerCase().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

        if (!behindSign.toLowerCase().equals(frontSign.toLowerCase())) {
            log.warn("系统级参数异常：sign校验失败!【frontSign:" + frontSign + ";behindSign" + behindSign + "】");
            return false;
        }
        return true;
    }

}
