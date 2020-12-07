package edu.njnu.reproducibility.common.enums;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 10:47
 * @modified By：
 * @version: 1.0.0
 */

/**
 * 自定义的异常code，请勿覆盖常见的HttpStatus.*.value()
 */


public enum ResultEnum {
    SUCCESS(0, "成功"),
    DEFAULT_EXCEPTION(-1, "默认的服务器内部异常，我并不想进行处理！！"),
    NO_OBJECT(-2, "没有对应的对象"),//没有对应的对象
    EXIST_OBJECT(-3, "对象已存在"),//对象已存在
    NO_TOKEN(-4, "Missing Token"),
    TOKEN_WRONG(-5, "Token Wrong"),
    USER_PASSWORD_NOT_MATCH(-6, "账户名和密码不匹配"),//账户名和密码不匹配
    QUERY_TYPE_ERROR(-7, "查询类型不支持"),//查询类型不支持
    REMOTE_SERVICE_ERROR(-8, "远程服务调用出错"),//远程服务调用出错
            ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
