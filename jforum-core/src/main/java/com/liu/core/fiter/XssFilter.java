package com.liu.core.fiter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.liu.core.config.xxs.XssRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 过滤器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 16:12
 */
public class XssFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(XssFilter.class);

    /**
     * 可放行的请求路径
     */
    private static final String IGNORE_PATH = "ignorePath";
    /**
     * 可放行的参数值
     */
    private static final String IGNORE_PARAM_VALUE = "ignoreParamValue";
    /**
     * 默认放行单点登录的登出响应(响应中包含samlp:LogoutRequest标签，直接放行)
     * 单点登录在退出时需要发送 <samlp:LogoutRequest xmlns:samlp.....
     */
    private static final String CAS_LOGOUT_RESPONSE_TAG = "samlp:LogoutRequest";
    /**
     * 可放行的请求路径列表
     */
    private List<String> ignorePathList;
    /**
     * 可放行的参数值列表
     */
    private List<String> ignoreParamValueList;

    /**
     * 初始化
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("XSS filter [XSSFilter] init start ...");
        String ignorePaths = filterConfig.getInitParameter(IGNORE_PATH);
        String ignoreParamValues = filterConfig.getInitParameter(IGNORE_PARAM_VALUE);
        if (!StrUtil.isBlank(ignorePaths)) {
            String[] ignorePathArr = ignorePaths.split(",");
            ignorePathList = Arrays.asList(ignorePathArr);
        }
        if (!StrUtil.isBlank(ignoreParamValues)) {
            String[] ignoreParamValueArr = ignoreParamValues.split(",");
            ignoreParamValueList = Arrays.asList(ignoreParamValueArr);
            //默认放行单点登录的登出响应(响应中包含samlp:LogoutRequest标签，直接放行)
            if (!ignoreParamValueList.contains(CAS_LOGOUT_RESPONSE_TAG)) {
                ignoreParamValueList.add(CAS_LOGOUT_RESPONSE_TAG);
            }
        } else {
            //默认放行单点登录的登出响应(响应中包含samlp:LogoutRequest标签，直接放行)
            ignoreParamValueList = new ArrayList<>();
            ignoreParamValueList.add(CAS_LOGOUT_RESPONSE_TAG);
        }
        log.debug("ignorePathList=" + JSONUtil.toJsonStr(ignorePathList));
        log.debug("ignoreParamValueList=" + JSONUtil.toJsonStr(ignoreParamValueList));
        log.debug("XSS filter [XSSFilter] init end");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("XSS filter [XSSFilter] starting");
        // 如果是 GET 请求直接放行
        if (isGetMethod((HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }
        // 判断uri是否包含项目名称
        String uriPath = ((HttpServletRequest) request).getRequestURI();
        if (isIgnorePath(uriPath)) {
            log.debug("ignore xssFilter,path[" + uriPath + "] pass through XssFilter, go ahead...");
            chain.doFilter(request, response);
            return;
        } else {
            log.debug("has xssFilter path[" + uriPath + "] need XssFilter, go to XssRequestWrapper");
            //传入重写后的Request
            chain.doFilter(new XssRequestWrapper((HttpServletRequest) request, ignoreParamValueList), response);
        }
        log.debug("XSS filter [XSSFilter] stop");
    }

    /**
     * 如果是 GET 请求直接放行
     */
    private boolean isGetMethod(HttpServletRequest request) {
        String method = request.getMethod();
        return method == null || HttpMethod.GET.matches(method);
    }

    @Override
    public void destroy() {
        log.debug("XSS filter [XSSFilter] destroy");
    }

    private boolean isIgnorePath(String servletPath) {
        if (StrUtil.isBlank(servletPath)) {
            return true;
        }
        if (CollectionUtil.isEmpty(ignorePathList)) {
            return false;
        } else {
            for (String ignorePath : ignorePathList) {
                if (!StrUtil.isBlank(ignorePath)) {
                    // /camunda/api/admin/auth/user/default/login/welcome
                    // /camunda/**  进行匹配
                    // 将带有 ** 的模式转换为正则表达式
                    ignorePath = ignorePath.replaceAll("\\*\\*", ".*");
                    // 将转换后的模式编译为正则表达式对象 即 /camunda/.*
                    Pattern regexPattern = Pattern.compile(ignorePath);
                    // 使用正则表达式匹配URL
                    Matcher matcher = regexPattern.matcher(servletPath);
                    // 匹配结果
                    if (matcher.find()) {
                        // 情况1 /camunda/api/admin/auth/user/default/login/welcome
                        // 情况2 xxx/camunda/api/admin/auth/user/
                        // 明显情况2不是想要的结果
                        // 如果长度一样则说明 匹配成功
                        String url = matcher.group(0);
                        return url.length() == servletPath.length();
                    }
                }
            }
        }
        return false;
    }
}
