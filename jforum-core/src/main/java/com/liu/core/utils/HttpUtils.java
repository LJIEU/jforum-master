package com.liu.core.utils;

import jakarta.servlet.ServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Description: 服务端发送请求
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:16
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送 Get 方法请求
     *
     * @param url         请求URL地址
     * @param param       请求参数
     * @param contentType 编码类型
     * @return 响应结果
     */
    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;

        try {
            String urlString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
            log.info("sendGet --- {}", urlString);

            // 创建 连接 实例
            URL realUrl = new URL(urlString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            // 连接
            connection.connect();

            // 读取返回的数据
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("访问结果 ++++ {}", realUrl);
        } catch (IOException e) {
            log.error("调用 HttpUtils.sendGet url=" + url + ",param=" + param, e);
        } finally {

            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("调用 HttpUtils.sendGet url=" + url + ",param=" + param, e);
            }
        }
        return result.toString();
    }

    @SuppressWarnings("all")
    public static String getBodyString(ServletRequest request) {
        // 获取请求体
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
//            reader = request.getReader();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.warn("获取响应体出现问题~");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("关闭出现问题~ ", e.getMessage());
                }
            }
        }
        return sb.toString();
    }
}
