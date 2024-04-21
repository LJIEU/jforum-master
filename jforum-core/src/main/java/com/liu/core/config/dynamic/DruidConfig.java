package com.liu.core.config.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import com.liu.core.constant.enume.DataSourceType;
import com.liu.core.utils.SpringUtils;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;

/**
 * Description: Druid配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 15:15
 */
@Slf4j
@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "camundaBpmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.camunda")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    // 该条件满足时才会启动配置注入 Bean 容器【这里不需要这样配置 因为配置了@Qualifier】
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(
            @Qualifier("primaryDataSource") DataSource primaryDataSource,
            @Qualifier("camundaBpmDataSource") DataSource camundaBpmDataSource,
            @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        /**
         * Spring 中的@Qualifier注解用于在有多个 Bean 与所需类型匹配时消除同一类型的多个 Bean 之间的歧义。
         * 您有多个相同类型的 Bean，并且您希望指定应将哪个 Bean 注入到特定组件或配置类中。这就是@Qualifier发挥作用的地方
         */
        HashMap<Object, Object> targetDataSources = new HashMap<>(2);
        // 添加多数据源
        targetDataSources.put(DataSourceType.MASTER.name(), primaryDataSource);

        slaveDataSource = SpringUtils.getBean("slaveDataSource");
        camundaBpmDataSource = SpringUtils.getBean("camundaBpmDataSource");
        if (slaveDataSource != null) {
            targetDataSources.put(DataSourceType.SLAVE.name(), slaveDataSource);
        }
        if (camundaBpmDataSource != null) {
            targetDataSources.put(DataSourceType.CAMUNDA.name(), camundaBpmDataSource);
        }

        // 默认数据源就是 主数据源【因为不管怎么样都需要一个默认的,然后再从多数据源中寻找目标数据源进行连接】
        return new DynamicDataSource(primaryDataSource, targetDataSources);
    }


    /**
     * 去除监控页面底部广告
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource.druid.stat-view-servlet", name = "enabled", havingValue = "true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties) {
        // 获取Web监控页面参数
        DruidStatProperties.StatViewServlet servlet = properties.getStatViewServlet();
        // 获取 common.js 配置路径
        String pattern = servlet.getUrlPattern() != null ? servlet.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        // 此文件在 com\alibaba\druid\1.2.18\druid-1.2.18.jar 依赖 里面 support\http\resources\js\common.js
        final String filePath = "support/http/resources/js/common.js";
        // 进行拦截过滤
        Filter filter = new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                Filter.super.init(filterConfig);
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                filterChain.doFilter(servletRequest, servletResponse);
                // 重置缓冲区
                servletResponse.resetBuffer();
                // 获取 common.js
                String text = Utils.readFromResource(filePath);
//                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
//                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                text = text.replaceAll("this.buildFooter\\(\\);", "");
                servletResponse.getWriter().write(text);
            }

            @Override
            public void destroy() {
                Filter.super.destroy();
            }
        };

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }

    @Value("${spring.datasource.druid.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.druid.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.druid.max-active}")
    private int maxActive;
    @Value("${spring.datasource.druid.max-wait}")
    private int maxWait;
    @Value("${spring.datasource.druid.connectTimeout}")
    private int connectTimeout;
    @Value("${spring.datasource.druid.socketTimeout}")
    private int socketTimeout;
    @Value("${spring.datasource.druid.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionTunsMillis;
    @Value("${spring.datasource.druid.min-evictable-idle-time-millis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.max-evictable-idle-time-millis}")
    private int maxEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource.druid.test-while-idle}")
    private boolean testWhileIdl;
    @Value("${spring.datasource.druid.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.druid.test-on-return}")
    private boolean testOnReturn;

    private DruidDataSource getDataSource(DruidDataSource dataSource) {
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setConnectTimeout(connectTimeout);
        dataSource.setSocketTimeout(socketTimeout);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionTunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdl);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        return dataSource;
    }

}
