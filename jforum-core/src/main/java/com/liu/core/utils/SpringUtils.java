package com.liu.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Description: Spring工具类 在非Spring管理环境中获取Bean
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:04
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext applicationContext;

    /**
     * 用于将给定的Bean工厂赋值给"beanFactory"静态成员变量
     * 这样其他类就可以使用该静态成员变量来访问Bean工厂
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 用于将应用程序上下文赋值给"applicationContext"静态成员变量
     * 这样其他类也可以使用该静态成员变量来访问应用程序上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }


    /**
     * 获取对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) {
        return (T) beanFactory.getBean(clz);
    }
}
