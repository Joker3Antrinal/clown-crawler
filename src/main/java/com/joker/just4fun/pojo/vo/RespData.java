package com.joker.just4fun.pojo.vo;

/**
 *
 * 内部接口响应
 *
 * @author Joker.Y
 * @since 2024/4/18
 * @version 1.0
 */
public record RespData<T>(int code, T data, String msg) {

}
