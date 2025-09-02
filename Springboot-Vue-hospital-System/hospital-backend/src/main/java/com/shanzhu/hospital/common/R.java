package com.shanzhu.hospital.common;


import lombok.Data;

/**
 * 接口返回对象封装 - Result
 *
 
 * @date: 2023-11-15
 */
@Data
public class R<T> {

    /**
     * 状态码
     */
    private int status;

    /**
     * 状态消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public R(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static R ok(Object data) {
        return new R(200, "成功", data);
    }

    public static R ok(String msg, Object data) {
        return new R(200, msg, data);
    }

    public static R ok(String msg) {
        return new R(200, msg, Boolean.TRUE);
    }

    public static R error(String msg) {
        return new R(400, msg, Boolean.FALSE);
    }
}
