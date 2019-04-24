package com.seebon.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.seebon.config.cache.EhcacheManager;
import com.seebon.config.constants.Constants;
import com.seebon.config.constants.SeeConfig;
import com.seebon.config.enums.Status;
import com.seebon.config.spring.environment.SeeConfigEnvironment;
import com.seebon.config.utils.SpringUtil;
import com.seebon.config.zookeeper.ConfigNode;
import com.seebon.config.zookeeper.ZookeeperChangeListener;
import com.seebon.config.zookeeper.ZookeeperClient;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 配置
 *
 * @author xfz
 */
public class SeeConfigContext {
    private final static Logger logger = LoggerFactory.getLogger(SeeConfigContext.class);

    private static class SingletonInstance {
        private static final SeeConfigContext SINGLETON = new SeeConfigContext();
    }

    public static SeeConfigContext getInstance() {
        return SeeConfigContext.SingletonInstance.SINGLETON;
    }

    private SeeConfigContext() {
        try {
            //节点路径
            String nodeDataPath = this.getNodePath();

            //添加节点
            ZookeeperClient.getInstance().register(nodeDataPath, 0, "");

            //绑定子节点变化事件器
//            ZookeeperClient.getInstance().addChildrenNodeListener(nodeDataPath, new ZookeeperChangeListener());
        } catch (Exception e) {
            logger.error("zookeeper 节点注册异常." + e.getMessage());
        }
    }

    public void mergeRemoteProperties(Properties properties) {
        //1、加载数据
        Properties remoteProperties = this.loadProperties();

        //2、合并属性
        remoteProperties.forEach((key, value) -> {
            //本地配置优先
            if (SeeConfig.INSTANCE.getLocalFirst() && properties.containsKey(key)) {
                return;
            }
            if (Constants.EMPTY_VALUE.equals(value)) {
                properties.setProperty(key.toString(), StringUtils.EMPTY);
            } else {
                properties.setProperty(key.toString(), value.toString());
            }
        });

        //3、设置环境配置
        this.refreshEnvironment(properties);
    }


    /**
     * 加载配置数据
     *
     * @return
     */
    private Properties loadProperties() {
        Properties properties = new Properties();
        //加载zookeeper节点配置
        Map<String, Object> configData = this.fetchConfigFromZookeeper();
        if (configData.isEmpty()) {
            //读取本地磁盘备份数据
            configData = this.fetchConfigFromEhcache();
        } else {
            //刷新缓存(磁盘)配置
            this.refreshEhcache(configData);
        }
        properties.putAll(configData);

        return properties;
    }


    /**
     * 加载zookeeper节点配置
     *
     * @return
     */
    private Map<String, Object> fetchConfigFromZookeeper() {
        Map<String, Object> result = Maps.newHashMap();
        try {
            //节点路径
            String path = this.getNodePath();

            List<String> childPaths = ZookeeperClient.getInstance().getChildrenPath(path);
            for (String childPath : childPaths) {
                byte[] data = ZookeeperClient.getInstance().getNodeData(String.format("%s/%s", path, childPath));
                if (data == null || data.length <= 0) {
                    continue;
                }
                //默认把正常数据都加载完成
                ConfigNode configNode = JSON.parseObject(new String(data, Constants.UTF_8), ConfigNode.class);
                if (configNode.getStatus() != null && Status.NORMAL.getType() == configNode.getStatus()) {
                    result.put(childPath, configNode.getKeyValue());
                }
            }
        } catch (Exception e) {
            logger.error("获取zookeeper节点数据异常." + e.getMessage(), e);
        }
        return result;
    }

    private String getNodePath() {
        return String.format("/%s/%s/%s", Constants.SEE_CONFIG, SeeConfig.INSTANCE.getProfile(), SeeConfig.INSTANCE.getAppName());
    }

    /**
     * 加载Ehcache缓存配置
     * <p>
     * 当zookeeper连接异常 则读取磁盘数据
     *
     * @return
     */
    private Map<String, Object> fetchConfigFromEhcache() {
        Map<String, Object> configMap = Maps.newHashMap();
        Cache<String, Object> cacheData = EhcacheManager.getInstance().getCache();
        cacheData.forEach(data -> {
            configMap.put(data.getKey(), data.getValue());
        });
        return configMap;
    }

    /**
     * 更新Ehcache缓存配置
     *
     * @return
     */
    private void refreshEhcache(Map<String, Object> config) {
        Cache<String, Object> cacheData = EhcacheManager.getInstance().getCache();
        //1、先清空缓存
        cacheData.clear();
        //2、重新设置缓存
        config.forEach((k, v) -> {
            cacheData.put(k, v);
        });
    }

    /**
     * 刷新环境配置
     * TODO 这是临时解决方案。问题描述：启动mongo权限认证配置后，zk添加监听与刷新Environment需要延后执行，否则启动报错。
     * 异常信息：java.lang.IllegalStateException: Cannot convert value of type [java.lang.String] to required type [com.mongodb.MongoCredential] for property 'credentials[0]': no matching editors or conversion strategy found
     * @param properties
     */
    private void refreshEnvironment(Properties properties) {
        new Thread(() -> {
                try {
                    Thread.sleep(60000);
                    ZookeeperClient.getInstance().addChildrenNodeListener(getNodePath(), new ZookeeperChangeListener());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                properties.forEach((key, value) -> {
//                    SeeConfigEnvironment.getInstance().refresh(key.toString(), value.toString());
//                });
        }).start();
    }
}
