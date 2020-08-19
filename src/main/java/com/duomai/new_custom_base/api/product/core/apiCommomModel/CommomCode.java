package com.duomai.new_custom_base.api.product.core.apiCommomModel;

import java.util.HashMap;

/**
 * 定义公共静态变量，错误代码，返回代码等
 * 徐云
 */
public class CommomCode {


    /**
     * API对象KEY值
     *
     */
    public static final String APIOBJ = "apiObj";

    /**
     * URL KEY值
     *
     */
    public static final String URL = "url";


    /**
     * token验证的key值定义
     *
     */
    public static final String TOKENKEY = "dmwxtoken";

    /**
     * API调用可重复
     */
    public static final int IS_TRANSFER_Y =0;

    /**
     * API调用不可重复
     */
    public static final int IS_TRANSFER_N =1;

    /**
     * 内部API
     */
    public static final int INTERNAL_API =2;

    /**
     * 加权API
     */
    public static final int WEIGHTED_API=1;

    /**
     * 开放API
     */
    public static final int OPEN_API=0;

    /**
     * IP白名单
     */
    public static final int WHITE_IP=0;

    /**
     * IP黑名单
     */
    public static final int BLACK_IP=1;

    /**
     * 普通IP
     */
    public static final int ORDINARY_IP=2;

    /**
     * 缓存时效(分钟)
     */
    public static final int REDIS_TIME=5;

    /**
     * 1分(min)=60000毫秒(ms)
     */
    public static final int MILLIS_TIME=60000;

    /**
     * appkey缓存到期时间(年) 缓存最多设置30天，0表示永不过期
     */
    public static final int LIST_EXP_APPKEY=30*24*60*60;
//    public static final int LIST_EXP_APPKEY=60;

    /**
     * appkeyList缓存到期时间(年) 缓存最多设置30天，0表示永不过期
     */
    public static final int LIST_EXP_API=0;

    /**
     * IP List缓存到期时间（永久时间，2年3年） 缓存最多设置30天，0表示永不过期
     */
    public static final int LIST_EXP_IP = 0 ;

    /**
     * 时间格式标准
     */
    public static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";

    /**
     * 成功（日志类型）
     */
    public static final int SUCCESS=0;

    /**
     * 异常 （日志类型）
     */
    public static final int ERROR=1;

    /**
     * 错误代码
     *     语法解析错误，服务端接收到无效的JSON
     */
    public static final String PARSE_ERROR="-32700";

    /**
     * 无效请求,发送的JSON内容不是一个有效的请求对象
     */
    public static final String INVALID_REQUEST="-32600";

    /**
     * 找不到方法,不存在或无效
     */
    public static final String METHOD_NOT_FOUND="-32601";

    /**
     * 无效的方法参数
     */
    public static final String INVALID_PARAMS="-32602";

    /**
     * JSON-RPC内部错误
     */
    public static final String INTERNAL_ERROR="-32603";


    //服务端错误（自定义错误-32000到-32099，其中-32768至-32000不能定义）
    /**
     * 请求服务IP为黑名单，拒绝请求
     */
    public static final String BLACK_IP_ERROR="-32001";

    /**
     * appKey无效或已过期
     *
     */
    public static final String APP_KEY_ERROR="-32002";

    /**
     * sign校验失败
     *
     */
    public static final String SIGN_ERROR="-32003";

    /**
     * 请求的API不存在或无权限使用
     *
     */
    public static final String API_ERROR="-32004";


    /**
     * 请求IP异常
     *
     */
    public static final String IP_ERROR="-32005";

    /**
     * 请求服务内部异常
     *
     */
    public static final String REQUEST_ERROR="-32006";

    /**
     * 缓存服务异常
     *
     */
    public static final String CACHE_ERROR="-32007";

    /**
     * 远程服务器异常
     *
     */
    public static final String SERVEER_ERROR="-32008";
    /**类型转换异常*/
    public static final String TYPE_ERROR="-32009";

    /**
     * token已过期或sessionkey已过期
     */
    public static final String TOKEN_OR_SESSIONKEY_EXPIRE="login";

    /**
     * sessionkey查询用户出错
     */
    public static final String INVALID_SESSIONKEY="-10001";

    /**
     * userId不能为空
     *
     */
    public static final String USER_ID_IS_NULL="-32101";

    /**
     * 卖家不存在
     *
     */
    public static final String SELLER_IS_NULL="-32102";

    /**
     * 用户nick不能为空
     *
     */
    public static final String USER_NICK_IS_NULL="-32103";

    /**
     * 混淆昵称或openuid转换为空
     *
     */
    public static final String BUYERNICK_OROPENUID_IS_NULL="-32104";
    /**
     * sign为空
     *
     */
    public static final String SIGN_IS_NULL="-32105";

    /**
     * 返回代码对应说明的集合
     */
    public static final HashMap<String,String> ApiErrorMap=new HashMap<String,String>(){
        {
            put("-32700","Parse error:语法解析错误");
            put("-32600","Invalid Request:无效请求");
            put("-32601","Method not found:找不到方法");
            put("-32602","Invalid Params:无效的方法参数");
            put("-32603","Internal Error:JSON-RPC内部错误");
            put("-32001","请求服务异常！");//其实为黑名单IP拒绝访问
            put("-32002","AppKey无效或已过期");
            put("-32003","sign校验失败");
            put("-32004","请求的API不存在或无权限使用");
            put("-32005","请求IP异常");
            put("-32006","请求服务内部异常");
            put("-32007","缓存服务异常");
            put("-32008","远程服务器异常");
            put("-32009","类型转换异常");
            put("-32101","userId不能为空");
            put("-32102","卖家不存在");
            put("-32103","用户nick不能为空");
            put("-32104","混淆昵称或openuid转换为空");
            put("-32105","sign为空");
        }
    };

}
