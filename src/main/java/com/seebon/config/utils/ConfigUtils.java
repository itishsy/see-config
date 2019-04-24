package com.seebon.config.utils;

import com.seebon.config.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * 工具类
 *
 * @author xufuzhou
 */
public final class ConfigUtils {

    /**
     * 替换value的${}
     *
     * @param value
     * @return
     */
    public static String replaceHolderValue(String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        if (value.startsWith(Constants.PLACEHOLDER_PREFIX) && value.endsWith(Constants.PLACEHOLDER_SUFFIX)) {
            return StringUtils.replaceEach(value, new String[]{Constants.PLACEHOLDER_PREFIX, Constants.PLACEHOLDER_SUFFIX}, new String[]{"", ""});
        }
        return StringUtils.EMPTY;
    }
}
