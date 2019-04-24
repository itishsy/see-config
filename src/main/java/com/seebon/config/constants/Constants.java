package com.seebon.config.constants;

/**
 * 常量
 *
 * @author xfz
 * @Date 2018年6月6日
 * @Version
 */
public final class Constants {
    /**
     * see-config
     */
    public static final String SEE_CONFIG = "see-config";

    public static final String PLACEHOLDER_PREFIX = "${";

    public static final String PLACEHOLDER_SUFFIX = "}";
    /**
     * UTF-8
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * 空值
     */
    public static final String EMPTY_VALUE = "<empty>";
    /**
     * 线程池大小
     */
    public static final int POOL_SIZE = 1;

    /**
     * 缓存别名
     */
    public static final String CACHE_ALIAS = "see_config_data_cache_alias";

    /**
     * 配置文件名称
     */
    public static final String CONFIG_FILE_NAME = "/ehcache-config.xml";

    /**============================zookeeper=============================*/
    /**
     * 会话超时时间
     */
    public static final int SESSION_TIMEOUTMS = 5000;

    /**
     * 连接超时时间
     */
    public static final int CONNECTION_TIMEOUTMS = 3000;

    /**
     * 默认连接同步超时时间
     */
    public final static int CONNECT_SYNC_TIMEOUT = 15 * 1000;

    /**
     * 监控线程池大小
     */
    public final static int CORE_POOL_SIZE = 20;

}
