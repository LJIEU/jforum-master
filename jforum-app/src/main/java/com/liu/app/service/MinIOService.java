package com.liu.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/15 15:50
 */
public interface MinIOService {
    /**
     * 文件上传
     * @param file 文件
     * @return 返回文件信息
     */
    Map<String, Object> upload(MultipartFile file);
}
