package com.seebon.config.callback;

import com.google.common.collect.Maps;
import com.seebon.config.utils.ConfigUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * value实例Holder
 *
 * @author xufuzhou
 */
public final class ConfigBeanHolder {
    private static final Logger logger = LoggerFactory.getLogger(ConfigBeanHolder.class);

    /**
     * 缓存所有@Value实例
     */
    public final static Map<String, Object> BEAN_CACHE_MAP = Maps.newConcurrentMap();

    /**
     * 刷新所有Bean的@value属性值
     *
     * @param keyName
     * @param keyValue
     */
    public static void refresh(final String keyName, final String keyValue) {
        ConfigBeanHolder.BEAN_CACHE_MAP.forEach((key, value) -> {
            ConfigBeanHolder.refreshBeanField(value, keyName, keyValue);
        });
    }

    /**
     * 刷新@value属性值
     *
     * @param bean
     * @param key
     * @param value
     */
    private static void refreshBeanField(final Object bean, final String key, final String value) {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);
                final Value annotation = field.getAnnotation(Value.class);
                if (null == annotation || Modifier.isFinal(field.getModifiers())) {
                    return;
                }
                String annotationValue = annotation.value();
                if (StringUtils.isBlank(annotationValue)) {
                    return;
                }
                if (ConfigUtils.replaceHolderValue(annotationValue).equals(key)) {
                    ConfigBeanHolder.setFieldValue(field, key, value);
                    logger.info("bean name [" + bean.getClass().getSimpleName() + "] field [" + field.getName() + "] new-value [" + value + "]");
                }
            }
        });
    }

    /**
     * set属性的值到Bean
     *
     * @param field
     * @param key
     * @param value
     */
    private static void setFieldValue(final Field field, final String key, final String value) throws IllegalArgumentException, IllegalAccessException {
        Type type = field.getType();
        if (type == Boolean.class || type == boolean.class) {
            field.setBoolean(key, Boolean.parseBoolean(value));
        } else if (type == Integer.class || type == int.class) {
            field.setInt(key, Integer.parseInt(value));
        } else if (type == Long.class || type == long.class) {
            field.setLong(key, Long.parseLong(value));
        } else if (type == Double.class || type == double.class) {
            field.setDouble(key, Double.parseDouble(value));
        } else if (type == Float.class || type == float.class) {
            field.setDouble(key, Float.parseFloat(value));
        } else if (type == Short.class || type == short.class) {
            field.setShort(key, Short.parseShort(value));
        } else if (type == Byte.class || type == byte.class) {
            field.setByte(key, Byte.parseByte(value));
        }
    }

}
