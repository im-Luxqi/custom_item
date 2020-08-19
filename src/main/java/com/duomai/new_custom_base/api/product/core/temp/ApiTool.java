package com.duomai.new_custom_base.api.product.core.temp;


import com.duomai.new_custom_base.api.product.core.pojo.ApiSysParameter;
import com.duomai.new_custom_base.common.tool.ApplicationUtils;
import com.duomai.new_custom_base.configures.SysProperties;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApiTool {
    public final static String ENCODING = "UTF-8";
    private static final org.slf4j.Logger loger = LoggerFactory.getLogger(ApiTool.class);

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * @param request
     * @return ip地址
     * @auothor：何佳伟
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 生成UUID，去掉横杠后的字符串
     *
     * @return
     * @auothor 何佳伟
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * JSON格式字符串消除换行符或回车符等
     *
     * @param
     * @return JSON格式字符串
     * @auothor：徐云
     */
    public static String deleteBr(String jsonParam) {
        String Param = jsonParam.replaceAll("\\r|\\n", "");
        return Param;
    }


    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }

    public static String readertostring(BufferedReader reader) {
        StringBuffer buffer = new StringBuffer();
        String line = " ";
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
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
            loger.warn("系统级参数异常：sign校验失败!【frontSign:" + frontSign + ";behindSign" + behindSign + "】");
            return false;
        }
        return true;
    }


    /**
     * 描述:获取请求头内容
     */
    private String getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        String result = "";
        for (String key : map.keySet()) {
            //System.out.println("key= "+ key + " and value= " + map.get(key));
            result = result + "key= " + key + " and value= " + map.get(key) + "\n";
        }
        return result;
    }


    /**
     * URLbase64加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encode(String data) throws Exception {
        byte[] b = UrlBase64.encode(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    /**
     * URLbase64解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decode(String data) throws Exception {
        byte[] b = UrlBase64.decode(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    /**
     * 16进制加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encode16(String data) throws Exception {
        byte[] b = Hex.encode(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    /**
     * 16进制解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decode16(String data) throws Exception {
        byte[] b = Hex.decode(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

}
