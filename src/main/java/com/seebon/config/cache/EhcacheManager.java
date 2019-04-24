package com.seebon.config.cache;


import com.seebon.config.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Ehcache缓存管理器
 *
 * @author xufuzhou
 */
public final class EhcacheManager implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(EhcacheManager.class);

    /**
     * 缓存实例
     */
    private CacheManager cacheManager = null;

    static {
        Runtime.getRuntime().addShutdownHook(new SeeConfigShutdownHook("SeeConfigEhcacheShutdownHook"));
    }

    private static class SingletonInstance {
        private static final EhcacheManager SINGLETON = new EhcacheManager();
    }

    public static EhcacheManager getInstance() {
        return EhcacheManager.SingletonInstance.SINGLETON;
    }

    private EhcacheManager() {
        super();
        cacheManager = initCacheManager();
    }

    private CacheManager initCacheManager() {
        if (cacheManager != null) {
            return cacheManager;
        }
        cacheManager = CacheManagerBuilder.newCacheManager(new XmlConfiguration(getClass().getResource(Constants.CONFIG_FILE_NAME)));
        cacheManager.init();
        return cacheManager;
    }

    public Cache<String, Object> getCache() {
        return this.cacheManager.getCache(Constants.CACHE_ALIAS, String.class, Object.class);
    }

    public Object getValue(final String key) {
        return this.getCache().get(key);
    }

    public boolean containsKey(final String key) {
        return this.getCache().containsKey(key);
    }

    public void refresh(String key, Object vaule) {
        if (StringUtils.isNotBlank(vaule.toString())) {
            this.getCache().put(key, vaule);
        } else {
            this.getCache().remove(key);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.cacheManager != null) {
            this.cacheManager.close();
        }
    }

    /**
     * 保证程序退出后把缓存数据刷到磁盘
     */
    private static class SeeConfigShutdownHook extends Thread {

        public SeeConfigShutdownHook(String threadName) {
            super(threadName);
        }

        @Override
        public void run() {
            logger.debug("Run shutdown hook now.");
            try {
                EhcacheManager.getInstance().close();
            } catch (Exception e) {
                logger.warn("Run shutdown hook now exception." + e.getMessage());
            }
        }
    }
}
