package com.liu.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Description: 获取IP地址
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:14
 */
public class IpUtils {

    /**
     * 匹配IP
     */
    private static final String REGX_0_255 = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
    private static final String REGX_IP = "((" + REGX_0_255 + "\\.){3}" + REGX_0_255 + ")";
    /**
     * 第一种 *.*.*.*
     * 或者 1.1.1.*
     * 或者 1.1.*.*
     * 或者 1.*.*.*
     */
    private static final String REGX_IP_WILDCARD = "(" +
            "((\\*\\.){3}\\*)|" +
            "(" + REGX_0_255 + "(\\.\\*){3})|" +
            "(" + REGX_0_255 + "\\." + REGX_0_255 + "(\\.\\*){2})|" +
            "((" + REGX_0_255 + "\\.){3}\\*)" +
            ")";
    /**
     * IP网段: 192.168.1.1-192.168.1.256
     */
    private static final String REGX_IP_SEG = "(" + REGX_IP + "\\-" + REGX_IP + ")";
    private static String ipAddress;

    /**
     * 获取 客户端IP
     */
    public static String getIpAddress() {
        return getIpAddress(ServletUtils.getRequest());
    }

    /**
     * 获取 客户端ip
     *
     * @return IP 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (Objects.equals(ip, "0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (ip != null && ip.indexOf(",") > 0) {
            String[] ips = ip.trim().split(",");
            for (String sub : ips) {
                if (!(StringUtils.isBlank(sub) || "unknown".equalsIgnoreCase(sub))) {
                    ip = sub;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 检查是否内部IP地址
     */
    public static boolean internalIp(String ip) {
        byte[] address = textToNumericFormatV4(ip);
        return internalIp(address) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] address) {
        // 检查IPv4地址字节数组是否不为空或包含的元素不少于 2 个
        if (!StringUtils.isNotBlank(Arrays.toString(address)) || address.length < 2) {
            return true;
        }

        // 提取地址前两个字节
        final byte b0 = address[0];
        final byte b1 = address[1];

        // 定义与每个私有 IP 地址范围的开头相对应的字节值
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;

        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                // 172.16.x.x/12 范围
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                // 192.168.x.x/16范围
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                // 否则不是 私有地址
                return false;
        }
    }

    /**
     * 将 IPv4 地址转字节
     *
     * @param text IPv4 地址
     * @return 字节 byte
     */
    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            // 如果IPv4地址为空 则返回null
            return null;
        }

        // 初始化一个大小为 4 的字节数组来存储 IP 地址字节
        byte[] bytes = new byte[4];

        // 使用.作为分隔符分割输入文本
        String[] elements = text.split("\\.");

        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    // 如果只有一个元素 则将其视为 32 位整数
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        //检查整数是否在有效的 IPv4 范围内
                        return null;
                    }
                    // 将 32 位整数转换为字节数组
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    // 如果有两个元素 则将它们视为两个 8 位整数
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    // 如果有三个元素 则将它们视为三个8位整数
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    // 如果有四个元素 则将它们视为四个 8 位整数
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    // 否则返回 null
                    return null;
            }
        } catch (NumberFormatException e) {
            // 处理将元素解析为整数失败 也返回 null
            return null;
        }
        //返回表示 IP 地址的结果字节数组
        return bytes;
    }


    /**
     * 校验 IP 是否符合过滤串规则
     *
     * @param filter 过滤IP列表 支持后缀*通配 支持网段如: 10.10.10.1-10.10.10.99
     * @param ip     校验IP地址
     * @return true 拦截  false 不拦截
     */
    public static boolean isMatchedIp(String filter, String ip) {
        if (StringUtils.isEmpty(filter) || StringUtils.isEmpty(ip)) {
            return false;
        }
        String[] filters = filter.split(";");
        for (String s : filters) {
            // 判断该字符串是否是 IP
            if (isIp(s) && s.equals(ip)) {
                return true;
            } else if (isIpWildCard(s) && ipIsInWildCardNoCheck(s, ip)) {
                return true;
            } else if (isIpSegment(s) && ipIsInNetNoCheck(s, ip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否在指定网段中
     */
    public static boolean ipIsInNetNoCheck(String ipArea, String ip) {
        // 1.1.1.1-1.1.1.255
        int idx = ipArea.indexOf('-');
        // 1.1.1.1
        String[] startIp = ipArea.substring(0, idx).split("\\.");
        // 1.1.1.255
        String[] endIp = ipArea.substring(idx + 1).split("\\.");
        // 1.1.1.4
        String[] targetIp = ip.split("\\.");
        long start = 0L, end = 0L, target = 0L;
        for (int i = 0; i < 4; i++) {
            start = start << 8 | Integer.parseInt(startIp[i]);
            end = end << 8 | Integer.parseInt(endIp[i]);
            target = target << 8 | Integer.parseInt(targetIp[i]);
        }
        return target >= start && target <= end;
    }

    /**
     * 是否特定格式 如:192.168.1.1-192.168.1.255
     */
    public static boolean isIpSegment(String ipSeg) {
        return StringUtils.isNotBlank(ipSeg) && ipSeg.matches(REGX_IP_SEG);
    }

    /**
     * 检查参数是否在IP通配符里
     * 127.0.0.1 就是在 127.*.*.* 里
     */
    public static boolean ipIsInWildCardNoCheck(String ipWildCard, String ip) {
        String[] s1 = ipWildCard.split("\\.");
        String[] s2 = ip.split("\\.");
        boolean flag = true;
        for (int i = 0; i < s1.length; i++) {
            if ("*".equals(s1[i])) {
                break;
            }
            // 如果直到遇到*的部分 存在不匹配说明不在该网段
            if (!s1[i].equals(s2[i])) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 是否为IP  * 为间隔的通配符地址 192.1.1.*
     */
    public static boolean isIpWildCard(String ip) {
        return StringUtils.isNotBlank(ip) && ip.matches(REGX_IP_WILDCARD);
    }

    /**
     * 是否为IP
     */
    public static boolean isIp(String ip) {
        return StringUtils.isNotBlank(ip) && ip.matches(REGX_IP);
    }
}
