package com.seebon.config.callback;

import com.seebon.config.zookeeper.ConfigNode;

/**
 * 刷新延迟生效的配置数据
 *
 * @author xfz
 */
public interface IDelayConfigRefresh {

    /**
     * 刷新延迟生效的配置数据
     *
     * @param configNode
     */
    void refreshDelayConfig(ConfigNode configNode);
}
