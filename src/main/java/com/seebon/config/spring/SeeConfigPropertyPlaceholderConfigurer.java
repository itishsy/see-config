package com.seebon.config.spring;

import com.seebon.config.SeeConfigContext;
import com.seebon.config.cache.EhcacheManager;
import com.seebon.config.callback.ConfigBeanHolder;
import com.seebon.config.constants.SeeConfig;
import com.seebon.config.utils.SpringUtil;
import com.seebon.config.zookeeper.ZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 配置Configurer
 *
 * @author xfz
 */
public class SeeConfigPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements BeanPostProcessor, DisposableBean, ApplicationContextAware {
    private final static Logger logger = LoggerFactory.getLogger(SeeConfigPropertyPlaceholderConfigurer.class);

    @Override
    protected Properties mergeProperties() throws IOException {
        Properties properties = super.mergeProperties();
        //配置初始化
        SeeConfig.INSTANCE.init(properties);
        if (SeeConfig.INSTANCE.getEnabled()) {
            //缓存初始化
            EhcacheManager.getInstance();
            //zookeeper初始化
            ZookeeperClient.getInstance().init(SeeConfig.INSTANCE.getHosts());
            //初始化配置
            SeeConfigContext.getInstance().mergeRemoteProperties(properties);
        }
        return properties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.setApplicationContext(applicationContext);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //拿取所有的fields
        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return bean;
        }
        //遍历所有的field，找出打上标记 @Value的field
        for (Field field : fields) {
            Value fieldReference = field.getAnnotation(Value.class);
            //如果field reference == null
            if (fieldReference == null) {
                continue;
            }
            ConfigBeanHolder.BEAN_CACHE_MAP.put(beanName, bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
    }

    @Override
    public void destroy() throws Exception {
        logger.info("SeeConfigPropertyPlaceholderConfigurer ======> destroy()");
        EhcacheManager.getInstance().close();
        ZookeeperClient.getInstance().close();
    }
}
