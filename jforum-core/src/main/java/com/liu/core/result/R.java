package com.liu.core.result;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.core.constant.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpEntity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: 返回结果集
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("unchecked")
public class R<T> extends HttpEntity<T> {

    /**
     * 状态码
     */
    public Integer code;
    /**
     * 信息
     */
    public String message;
    /**
     * 数据
     */
    public T data;

    public R(T body) {
        super(body);
    }


    public static <T> Map<String, Object> body(Integer code, String message, T data) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (!String.valueOf(code).equals("")) {
            map.put("code", code.toString());
        }
        if (StrUtil.isNotBlank(message)) {
            map.put("message", message);
        }
        if (ObjectUtil.isNotEmpty(data)) {
            map.put("data", data);
        }
        return map;
    }

    /**
     * 返回成功
     */
    public static <T> R<T> success() {
        return new R<>((T) body(HttpStatus.SUCCESS, "成功~", null));
    }

    /**
     * 返回成功带结果集
     */
    public static <T> R<T> success(T data) {
//        return new R<>(HttpStatus.SUCCESS, "成功~", data);
        return new R<>((T) body(HttpStatus.SUCCESS, "成功~", data));
    }

    /**
     * 返回成功带自定义描述 和 结果集
     */
    public static <T> R<T> success(String message, T data) {
        return new R<>((T) body(HttpStatus.SUCCESS, message, data));
    }

    /**
     * 返回成功带 返回状态码 和 结果集
     */
    public static <T> R<T> success(int code, T data) {
        return new R<>((T) body(code, "成功~", data));
    }

    /**
     * 返回失败
     */
    public static <T> R<T> fail() {
        return new R<>((T) body(HttpStatus.BAD_REQUEST, "_失败_", null));
    }

    /**
     * 返回失败带失败信息
     */
    public static <T> R<T> fail(String message) {
        return new R<>((T) body(HttpStatus.BAD_REQUEST, message, null));
    }

    /**
     * 返回失败带状态码及失败信息
     */
    public static <T> R<T> fail(int code, String message) {
        return new R<>((T) body(code, message, null));
    }

    /**
     * 返回失败带状态码及错误数据
     */
    public static <T> R<T> fail(int httpStatus, T data) {
        return new R<>((T) body(httpStatus, "_失败_", data));
    }
}