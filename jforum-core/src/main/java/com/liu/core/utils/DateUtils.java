package com.liu.core.utils;

import java.text.ParseException;
import java.util.Date;

/**
 * Description: 时间格式处理类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 9:51
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    // 一系列解析模式
    private static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-ddHH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy/MM/dd HH/mm/ss",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",};

    public static Date parseDate(String str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 通过 ms 时间 进行格式转换
     *
     * @param costTime 消耗时间
     * @return 返回 字符串 xxx ms  xx s  xx minus
     */
    public static String parseCostTime(Long costTime) {
        long time = 0;
        try {
            time = costTime;
        } catch (Exception ignored) {
            return "0 ms";
        }
        if (time / 1000 > 0) {
            // 分钟
            if (time / 1000 / 60 > 0) {
                return time / 1000 / 60 + " minute";
            } else {
                // 秒
                return time / 1000 + " s";
            }
        }
        // 毫秒
        return costTime + " ms";
    }
}
