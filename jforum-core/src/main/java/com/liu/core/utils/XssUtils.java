package com.liu.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 16:09
 */
public class XssUtils {
    private static final Logger log = LoggerFactory.getLogger(XssUtils.class);


    /**
     * Antisamy文件名
     */
    private static final String ANTISAMY_PATH = "antisamy-slashdot.xml";

    public static Policy policy = null;

    /*
      加载 Antisamy 文件
     */
    static {
        log.debug(" start read XSS configFile [" + ANTISAMY_PATH + "]");
        // 读取策略文件
        InputStream inputStream = XssUtils.class.getClassLoader().getResourceAsStream(ANTISAMY_PATH);
        try {
            policy = Policy.getInstance(Objects.requireNonNull(inputStream));
            log.debug("read XSS configFile [" + ANTISAMY_PATH + "] success");
        } catch (PolicyException e) {
            log.error("read XSS configFile [" + ANTISAMY_PATH + "] fail , reason:", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close XSS configFile [" + ANTISAMY_PATH + "] fail , reason:", e);
                }
            }
        }
    }

    /**
     * 跨站攻击语句过滤 方法
     *
     * @param paramValue           待过滤的参数
     * @param ignoreParamValueList 忽略过滤的参数列表
     */
    public static String xssClean(String paramValue, List<String> ignoreParamValueList) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            log.debug("raw value before xssClean: " + paramValue);
            if (isIgnoreParamValue(paramValue, ignoreParamValueList)) {
                log.debug("ignore the xssClean,keep the raw paramValue: " + paramValue);
                return paramValue;
            } else {
                final CleanResults cr = antiSamy.scan(paramValue, policy);
                cr.getErrorMessages().forEach(log::debug);
                String str = cr.getCleanHTML();
                log.debug("xssFilter value after xssClean" + str);
                return str;
            }
        } catch (ScanException e) {
            log.error("scan failed antisamy is [" + paramValue + "]", e);
        } catch (PolicyException e) {
            log.error("antisamy convert failed antisamy is [" + paramValue + "]", e);
        }
        return paramValue;
    }

    /**
     * 忽略参数值列表
     *
     * @param paramValue           请求值
     * @param ignoreParamValueList 忽略参数列表
     */
    private static boolean isIgnoreParamValue(String paramValue, List<String> ignoreParamValueList) {
        if (StrUtil.isBlank(paramValue)) {
            return true;
        }
        if (CollectionUtil.isEmpty(ignoreParamValueList)) {
            return false;
        } else {
            for (String ignoreParamValue : ignoreParamValueList) {
                if (paramValue.contains(ignoreParamValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
