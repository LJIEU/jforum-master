package com.liu.core.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description: 归属地获取
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:13
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP 地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIp(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }

        // 去外网查询获取地址
        String response = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
        if (StringUtils.isNotEmpty(response)) {
            JSONObject object = JSON.parseObject(response);
            // 省份
            String province = object.getString("pro");
            // 城市
            String city = object.getString("city");
            return String.format("%s %s", province, city);
        } else {
            log.error("获取地理位置异常 {}", ip);
            return UNKNOWN;
        }
    }
}
