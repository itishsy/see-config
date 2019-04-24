package com.seebon.config.utils;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring容器的bean工具类
 *
 * @author xfz 2018年6月7日
 */
public class SpringUtil {

    /**
     * 设置上下文为静态变量
     */
    private static ApplicationContext context;

    /**
     * 设置容器上下文【仅可设置一次】
     *
     * @param ctx
     */
    public static void setApplicationContext(ApplicationContext ctx) {
        if (context == null) {
            context = ctx;
        }
    }

    /**
     * 获取spring上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 按Class首字母小写的Bean名及Class类型
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 动态注册bean到spring容器 bean名为首字母小写的类名
     *
     * @param cls
     */
    public static void registerBean(Class<?> cls) {
        String beanName = SpringUtil.getBeanName(cls);
        if (!context.containsBean(beanName)) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cls);
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
            BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
            beanDefinitonRegistry.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
        }
    }

    /**
     * 从spring容器移除已注册的bean bean名为首字母小写的类名
     *
     * @param cls
     */
    public static void removeBean(Class<?> cls) {
        String beanName = SpringUtil.getBeanName(cls);
        if (context.containsBean(beanName)) {
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
            BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
            beanDefinitonRegistry.removeBeanDefinition(beanName);
        }
    }

    private static String getBeanName(Class<?> cls) {
        return String.format("%s%s", cls.getSimpleName().substring(0, 1).toLowerCase(), cls.getSimpleName().substring(1));
    }
}
