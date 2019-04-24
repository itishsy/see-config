package com.seebon.config.callback;

import com.seebon.config.zookeeper.ConfigNode;

/**
 * 配置变更
 *
 * @author xfz
 */
public interface IConfigChangeCallBack {
    /**
     * 配置数据发生改变
     *
     * @param configNode
     */
    void onConfigChanged(ConfigNode configNode);
}
