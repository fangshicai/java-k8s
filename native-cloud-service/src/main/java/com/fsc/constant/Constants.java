package com.fsc.constant;

/**
 * 常用的常量
 * @author zhai
 * @version 1.0.0
 * @ClassName Constants.java
 * @createTime 2021年12月22日 14:53:00
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 失败标记
     * 没有或缺少 AppID 或 AppSECRET
     */
    public static final Integer NO_KEY = 5217;

    /**
     * 失败标记
     * 接口使用次数不够
     */
    public static final Integer API_NUM = 5311;

    /**
     * 失败标记
     * 请求参数不对
     */
    public static final Integer PARAM_ERROR = 5314;

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 2;

    /**
     * SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
     * 上下文用户的默认是
     */
    public static final String CONTEXT_USER = "anonymousUser";

    /**
     * 逻辑删除状态 1 启用 0禁用
     */
    public static final Byte ACTIVE = 1;
    public static final Byte DISABLE = 0;

    /**
     * 统计用户总数
     */
    public static final String  USER_COUNT_NAME= "统计用户总数";
    /**
     * 近七天新增用户总数
     */
    public static final String  USER_SEVEN_DAYS_ADD_COUNT_NAME= "近七天新增用户数";

    /**
     * 可调用的api模块数
     */
    public static final String  API_COUNT_NAME= "可调用的api模块数";
    /**
     * 近七天新增api模块数
     */
    public static final String  API_SEVEN_DAYS_ADD_COUNT_NAME= "近七天新增api模块数";
    /**
     * 用户已经申请的应用总数
     */
    public static final String  USER_APP_COUNT_NAME= "用户申请的应用总数";
    /**
     * 用户近七天申请的应用总数
     */
    public static final String  USER_APP_SEVEN_DAYS_ADD_COUNT_NAME= "用户近七天申请的应用数";

    /**
     * 用户已经调用的api总数
     */
    public static final String  USER_APP_RE_COUNT_NAME= "用户请求api调用的总数";
    /**
     * 近七天调用api请求数
     */
    public static final String  USER_APP_SEVEN_DAYS_RE_COUNT_NAME= "近七天调用api请求数";

    /**
     * 返回给前端的日期list名
     */
    public static final String RES_DATELIST_NAME="dateList";
    /**
     * 返回给前端的统计数组名
     */
    public static final String RES_COUNTLIST_NAME="countList";
    /**
     * 返回给前端的的时间
     */
    public static final String RES_DATE_NAME="resDate";
    /**
     * 返回给前端的标签名
     */
    public static final String RES_LABEL_NAME="labelName";
    /**
     * 返回给前端的是否有数据
     */
    public static final String RES_DATASUM_NAME="sum";


}
