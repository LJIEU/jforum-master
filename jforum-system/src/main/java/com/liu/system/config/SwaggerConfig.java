package com.liu.system.config;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: Swagger配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/31 12:55
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"com.liu"};
        return GroupedOpenApi.builder().group("所有模块")
                .pathsToMatch(paths)
                .addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(
                        new HeaderParameter()
                                .name("authorization")
                                .example("token_xxx")
                                .description("网关JWT")
                                .in(String.valueOf(ParameterIn.HEADER))
                                .schema(new StringSchema()
                                        ._default("xxx")
                                        .name("authorization")
                                        .description("权限校验"))))
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public GroupedOpenApi toolsApi() {
        String[] paths = {"/tools/**"};
        String[] packagedToMatch = {"com.liu.generator"};
        return GroupedOpenApi.builder().group("工具模块")
                .pathsToMatch(paths)
                .addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(
                        new HeaderParameter()
                                .name("authorization")
                                .example("token_xxx")
                                .description("网关JWT")
                                .in(String.valueOf(ParameterIn.HEADER))
                                .schema(new StringSchema()
                                        ._default("xxx")
                                        .name("authorization")
                                        .description("权限校验"))))
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JForum系统API")
                        .version("1.0")
                        .description("Knife4j—OpenApi3文档")
                        .contact(new Contact().name("JIE")
                                .email("2353471003@qq.com"))
                        .license(new License().name("Apache 2.0")
                                .url("https: //www.apache.orq/licenses/LICENSE-2"))
                ).addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(new Components().addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme()
                        .name(HttpHeaders.AUTHORIZATION).type(SecurityScheme.Type.HTTP).scheme("bearer")));
    }


    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return 全局开放API定制器
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return new GlobalOpenApiCustomizer() {
            @Override
            public void customise(OpenAPI openApi) {
                if (openApi.getTags() != null) {
                    openApi.getTags().forEach(tag -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("x-order", RandomUtil.randomInt(1, 100));
                        tag.setExtensions(map);
                    });
                }
                if (openApi.getPaths() != null) {
                    openApi.addExtension("x-test123", "333");
                    openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
                }
            }
        };
    }
}
