package com.seebon.config.utils;

import com.seebon.config.cache.EhcacheManager;
import com.seebon.config.exception.SeeConfigException;
import com.seebon.config.spring.environment.SeeConfigEnvironment;
import org.apache.commons.lang3.StringUtils;

/**
 * 配置获取工具
 *
 * @author xfz
 */
public final class SeeConfigUtils {
    private static final String ERROR_MSG = "config key [ %s ] does not exist";

    public static Object get(final String key) {
        return SeeConfigUtils.get(key, StringUtils.EMPTY);
    }

    public static String getStr(String key) {
        Object value = SeeConfigUtils.get(key);
        SeeConfigUtils.error(value, key);
        return value.toString();
    }

    public static int getInt(String key) {
        Object value = SeeConfigUtils.get(key);
        SeeConfigUtils.error(value, key);
        return Integer.parseInt(value.toString());
    }

    public static long getLong(String key) {
        Object value = SeeConfigUtils.get(key);
        SeeConfigUtils.error(value, key);
        return Long.parseLong(value.toString());
    }

    public static boolean getBoolean(String key) {
        Object value = SeeConfigUtils.get(key);
        SeeConfigUtils.error(value, key);
        return Boolean.valueOf(value.toString());
    }

    public static Object get(String key, String defaultVal) {
        //先去读内存数据
        Object value = SeeConfigEnvironment.getInstance().getProperty(key);
        if (value != null) {
            return value;
        }
        //读取磁盘数据
        value = EhcacheManager.getInstance().getValue(key);
        if (value != null) {
            return value;
        }
        return defaultVal;
    }

    public static boolean contains(final String key) {
        //先去读内存数据
        boolean flag = SeeConfigEnvironment.getInstance().containsProperty(key);
        if (!flag) {
            //读取磁盘数据
            flag = EhcacheManager.getInstance().containsKey(key);
        }
        return flag;
    }

    private static void error(Object value, String key) {
        if (value == null) {
            throw new SeeConfigException(String.format(ERROR_MSG, key));
        }
    }
}
