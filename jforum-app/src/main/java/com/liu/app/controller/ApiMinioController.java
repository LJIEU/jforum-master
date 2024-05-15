package com.liu.app.controller;

import com.liu.app.service.MinIOService;
import com.liu.core.config.repeat.RepeatSubmit;
import com.liu.core.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/15 15:48
 */
@Slf4j
@Tag(name = "对象存储管理")
@RequestMapping("/app/myMinio")
@RestController
public class ApiMinioController {
    @Autowired
    private MinIOService minIOService;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    @RepeatSubmit
    public R<Map<String, Object>> uploadFile(@RequestPart("pictureFile") MultipartFile file) {
        Map<String, Object> map = minIOService.upload(file);
        return R.success(map);
    }
}
