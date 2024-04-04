package com.liu.core.excption;

import com.liu.core.constant.HttpStatus;
import com.liu.core.result.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 全局异常处理器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 14:57
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(Exception.class)
    public R exceptionHandler(Exception e, HttpServletRequest request) {
        if (e instanceof MethodArgumentNotValidException m) {
            // 参数校验异常
            Map<String, String> map = new HashMap<>();
            BindingResult result = m.getBindingResult();
            result.getFieldErrors().forEach((item) -> {
                String message = item.getDefaultMessage();
                String field = item.getField();
                map.put(field, message);
            });
//            log.error("请求地址'{}',数据校验发现错误:{}", request.getRequestURI(), e);
            logErrorWithRequestURI(request, "数据校验发现错误", e);
            return R.fail(HttpStatus.BAD_REQUEST, map);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            logErrorWithRequestURI(request, "请求方法错误", e);
            return R.fail(HttpStatus.BAD_REQUEST, "请求方法不正确");
        } else {
            logErrorWithRequestURI(request, e.getMessage(), e);
            return R.fail(HttpStatus.SYSTEM_ERROR, e.getMessage());
        }
    }

    @ExceptionHandler(MyBatisSystemException.class)
    public R<String> exceptionHandler(MyBatisSystemException e, HttpServletRequest request) {
        logErrorWithRequestURI(request, "Mybatis有误!", e);
        return R.fail(HttpStatus.SYSTEM_ERROR, "服务器异常");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R<String> exceptionHandler(AccessDeniedException e, HttpServletRequest request) {
        logErrorWithRequestURI(request, "权限不足!", null);
        return R.fail(HttpStatus.UNAUTHORIZED, "无权限");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<String> exceptionHandler(ServiceException e, HttpServletRequest request) {
        logErrorWithRequestURI(request, e.getMessage());
        Integer code = e.getCode();
        if (code == null) {
            code = HttpStatus.SYSTEM_ERROR;
        }
        return R.fail(code, e.getMessage());
    }

    @ExceptionHandler(RepeatSubmitException.class)
    public R<String> exceptionHandler(RepeatSubmitException e, HttpServletRequest request) {
        logErrorWithRequestURI(request, e.getMessage());
        if (e.getCode() == null) {
            e.setCode(HttpStatus.SUCCESS);
        }
        return R.fail(e.getCode(), e.getMessage());
    }

    private void logErrorWithRequestURI(HttpServletRequest request, String message, Exception e) {
        log.error("\n请求地址'{}',{}:{}", request.getRequestURI(), message, e);
    }

    private void logErrorWithRequestURI(HttpServletRequest request, String message) {
        log.error("\n请求地址'{}',{}", request.getRequestURI(), message);
    }
}
