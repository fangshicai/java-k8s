package com.fsc.constant;

/**
 * @author zhai
 * @version 1.0.0
 * @ClassName ErrorEnums.java
 * @createTime 2022年01月18日 13:46:00
 */
public enum ErrorEnums {

    SUCCESS(200,"请求成功"),
    FAIL(500, "服务器内部错误"),
    NO_KEY(5217, "没有或缺少 appId 或 appSecret"),
    API_NUM(5311, "接口使用次数不够"),
    PARAM_ERROR(5314, "请求参数不对"),
    NOT_FOUND(404, "资源找不到"),
    UNAUTHORIZED(401, "未授权"),
    ;

    @Override
    public String toString() {
        return "ErrorEnums{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    int code;
    String msg;

    ErrorEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
