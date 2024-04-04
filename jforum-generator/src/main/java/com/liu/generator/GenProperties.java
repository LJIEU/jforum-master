package com.liu.generator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: 读取配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 16:42
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "gen")
//@PropertySource(value = {"classpath:generator.yaml"})
public class GenProperties {
    /**
     * 作者
     */
    public String author;

    /**
     * 生成包路径
     */
    public String packageName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 自动去除表前缀，默认是false
     */
    public boolean autoRemovePre;

    /**
     * 表前缀(类名不会包含表前缀)
     */
//    @Value("${tablePrefix}")
    public String tablePrefix;

}

