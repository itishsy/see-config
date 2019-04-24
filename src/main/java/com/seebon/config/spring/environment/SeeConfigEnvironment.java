package com.seebon.config.spring.environment;

import com.google.common.collect.Maps;
import com.seebon.config.constants.Constants;
import com.seebon.config.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.*;

import java.util.Map;
import java.util.Properties;

/**
 * 配置环境
 *
 * @author xfz
 */
public class SeeConfigEnvironment {
    private static final MapPropertySource SOURCE = new MapPropertySource(Constants.SEE_CONFIG, Maps.newLinkedHashMap());

    private ConfigurableEnvironment environment;

    private static class SingletonInstance {
        private static final SeeConfigEnvironment SINGLETON = new SeeConfigEnvironment();
    }

    public static SeeConfigEnvironment getInstance() {
        return SeeConfigEnvironment.SingletonInstance.SINGLETON;
    }

    private SeeConfigEnvironment() {
        this.environment = SpringUtil.getBean(StandardEnvironment.class);
        this.environment.getPropertySources().addLast(SOURCE);
    }

    /**
     * 刷新环境配置
     *
     * @param key
     * @param value
     */
    public synchronized void refresh(String key, String value) {
        MutablePropertySources propertySources = environment.getPropertySources();

        MapPropertySource mapPropertySource = (MapPropertySource) propertySources.get(Constants.SEE_CONFIG);
        if (mapPropertySource == null) {
            return;
        }
        Map<String, Object> sourceMap = mapPropertySource.getSource();
        sourceMap.put(key, StringUtils.defaultString(value, StringUtils.EMPTY));

        Properties properties = new Properties();
        properties.putAll(sourceMap);

        propertySources.replace(SOURCE.getName(), new PropertiesPropertySource(SOURCE.getName(), properties));
    }

    /**
     * 获取环境配置
     */
    public Object getProperty(String key) {
        return environment.getProperty(key, Object.class);
    }

    /**
     * 是否存在key
     */
    public boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }
}
