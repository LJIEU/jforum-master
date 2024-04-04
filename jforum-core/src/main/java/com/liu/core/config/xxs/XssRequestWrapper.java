package com.liu.core.config.xxs;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.core.utils.XssUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Description: 包装器获取body数据
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 16:11
 */
@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper {
    private final List<String> ignoreParamValueList;

    public XssRequestWrapper(HttpServletRequest request, List<String> ignoreParamValueList) {
        super(request);
        this.ignoreParamValueList = ignoreParamValueList;
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @SuppressWarnings("all")
    @Override
    public ServletInputStream getInputStream() throws IOException {
        String json = IOUtils.toString(super.getInputStream(), StandardCharsets.UTF_8);
        // 如果不是JSON请求也返回
        if (!isJsonRequest())
            return super.getInputStream();
        // 为空 直接返回
        if (StrUtil.isEmpty(json))
            return super.getInputStream();
        // xss过滤
        json = XssUtils.xssClean(json, ignoreParamValueList);
        // 将转为转义字符的字符串转回原字符
        json = EscapeUtil.unescapeHtml4(json);
        byte[] jsonBytes = json.getBytes("utf-8");
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonBytes);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public int available() throws IOException {
                return jsonBytes.length;
            }
        };
    }


    /**
     * 是否是Json请求
     */
    public boolean isJsonRequest() {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * /xxx?id=1&id=2&name=3
     * 获取访问请求的所有参数以及对应的组合==》Map集合 每一个参数的值是一个数组 request.getParameterMap() ==> [1,2][3]
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> requestMap = super.getParameterMap();
        for (Map.Entry<String, String[]> me : requestMap.entrySet()) {
            log.debug(me.getKey() + ":");
            String[] values = me.getValue();
            for (int i = 0; i < values.length; i++) {
                log.debug(values[i]);
                values[i] = XssUtils.xssClean(values[i], this.ignoreParamValueList);
            }
        }
        return requestMap;
    }

    /**
     * /xxx?id=1&id=2
     * 获取参数值数组 一个参数多个值  ==》 request.getParameterValues("id") ==> 1,2
     */
    @Override
    public String[] getParameterValues(String paramString) {
        String[] arrayOfString1 = super.getParameterValues(paramString);
        if (arrayOfString1 == null) {
            return null;
        }
        int i = arrayOfString1.length;
        String[] arrayOfString2 = new String[i];
        for (int j = 0; j < i; j++) {
            arrayOfString2[j] = XssUtils.xssClean(arrayOfString1[j], this.ignoreParamValueList);
        }
        return arrayOfString2;
    }

    /**
     * /xxx?id=1&id=2
     * 根据传递参数名 获取页面请求的参数值  ==》 request.getParameter("id") ==> 1
     * 如果多个重名参数 只会取第一个值
     */
    @Override
    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (str == null) {
            return null;
        }
        return XssUtils.xssClean(str, this.ignoreParamValueList);
    }


    /**
     * 获取请求头的值  ==> request.getHeader("Host") ==> xxx.xxx.xxx.xxx
     */
    @Override
    public String getHeader(String paramString) {
        String str = super.getHeader(paramString);
        if (str == null) {
            return null;
        }
        return XssUtils.xssClean(str, this.ignoreParamValueList);
    }
}