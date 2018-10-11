package com.cx.logclient.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 获取springcontext
 *
 * @author zhongkewen
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private static BeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String name) {
        return applicationContext.getBean(name, clazz);
    }

    public static String getProperty(String key) {
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
        return relaxedPropertyResolver.getProperty(key, String.class);
    }

    public static <T> T getProperty(String key, Class<T> clazz) {
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
        return (T) relaxedPropertyResolver.getProperty(key, clazz);
    }

    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
        return (T) relaxedPropertyResolver.getProperty(key, clazz, defaultValue);
    }

    public static Map<String, Object> getPropertyMap(String prefix) {
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
        return relaxedPropertyResolver.getSubProperties(prefix);
    }
}
