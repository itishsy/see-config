package com.seebon.config.constants;

import com.seebon.config.exception.SeeConfigException;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * 配置
 *
 * @author xfz
 */
public enum SeeConfig {
    INSTANCE;

    /**
     * zookeeper hosts
     */
    private String hosts;

    /**
     * 当前环境
     */
    private String profile;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 本地配置优先
     */
    private boolean localFirst = Boolean.TRUE;

    /**
     * 是否启用配置中心，默认：true
     */
    private boolean enabled = Boolean.TRUE;

    public void init(String hosts, String profile, String appName, boolean localFirst, boolean enabled) {
        this.hosts = hosts;
        this.profile = profile;
        this.appName = appName;
        this.localFirst = localFirst;
        this.enabled = enabled;
    }

    public String getHosts() {
        return hosts;
    }

    public String getProfile() {
        return profile;
    }

    public String getAppName() {
        return appName;
    }

    public boolean getLocalFirst() {
        return localFirst;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void init(Properties prop) {
        String enabled = StringUtils.defaultString(this.getProperty(prop, "see.config.client.enabled"), "true");
        String profile = this.getProperty(prop, "see.config.client.profile");
        String appName = this.getProperty(prop, "see.config.client.appName");
        String hosts = this.getProperty(prop, "see.config.client.hosts");
        if (StringUtils.isBlank(hosts) || StringUtils.isBlank(profile) || StringUtils.isBlank(appName)) {
            throw new SeeConfigException("初始化see config失败，原因[hosts|profile|appName 为空，请仔细检查配置是否配置好........]");
        }
        if (StringUtils.isBlank(System.getProperty("see.config.client.appName"))) {
            String systemAppName = System.getProperty("appName");
            if (StringUtils.isBlank(systemAppName)) {
                System.setProperty("see.config.client.appName", appName);
            } else {
                System.setProperty("see.config.client.appName", systemAppName);
            }
        }
        this.init(hosts, profile, appName, Boolean.TRUE, Boolean.valueOf(enabled));
    }

    private String getProperty(Properties prop, String key) {
        String value = System.getProperty(key);
        if (StringUtils.isBlank(value)) {
            value = prop.getProperty(key);
        }
        return value;
    }
}
