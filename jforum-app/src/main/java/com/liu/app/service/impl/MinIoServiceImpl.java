package com.liu.app.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.liu.app.service.MinIOService;
import com.liu.db.domain.BucketPolicyConfig;
import io.minio.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/15 15:50
 */
@Service
public class MinIoServiceImpl implements MinIOService {

    @Value("${minio.endpoint}")
    private String ENDPOINT;

    @Value("${minio.bucketName}")
    private String BUCKET_NAME;

    @Value("${minio.accessKey}")
    private String ACCESS_KEY;

    @Value("${minio.secretKey}")
    private String SECRET_KEY;


    /**
     * 上传文件
     *
     * @return 返回文件结果
     */
    @Override
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>(2);

        try {
            // 创建MinIO客户端实例
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();

            // 检查存储桶是否存在，如果不存在就创建它
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }

            // 生成唯一的对象名称
            String objectName = UUID.randomUUID() + "/" + file.getOriginalFilename();

            // 将文件上传到 MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());

            // 生成文件 URL
            String fileUrl = ENDPOINT + "/" + BUCKET_NAME + "/" + objectName;
            map.put("url", fileUrl);

            // 为上传的文件添加公共读取访问权限
            BucketPolicyConfig bucketPolicyConfigDto = createBucketPolicyConfigDto(BUCKET_NAME);
            SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                    .bucket(BUCKET_NAME)
                    .config(JSONUtil.toJsonStr(bucketPolicyConfigDto))
                    .build();
            minioClient.setBucketPolicy(setBucketPolicyArgs);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "File upload failed");
        }
        return map;
    }


    /**
     * 创建存储桶的访问策略，设置为只读权限
     */
    @NotNull
    private BucketPolicyConfig createBucketPolicyConfigDto(String bucketName) {
        BucketPolicyConfig.Statement statement = new BucketPolicyConfig.Statement();
        statement.setEffect("Allow");
        statement.setPrincipal("*");
        statement.setAction("s3:GetObject");
        statement.setResource("arn:aws:s3:::" + bucketName + "/*");

        BucketPolicyConfig bucketPolicyConfig = new BucketPolicyConfig();
        // 设置固定版本或更新以匹配 MinIO 的预期格式 写死的 这个是版本策略日期
        bucketPolicyConfig.setVersion("2012-10-17");
        bucketPolicyConfig.setStatement(Collections.singletonList(statement));
        return bucketPolicyConfig;
    }

    /**
     * 因为 SimpleDateFormat 是线程不安全的
     * 下列代码保证线程安全
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static String formatDate(Date date) {
        return DATE_FORMATTER.format(date.toInstant());
    }


}
