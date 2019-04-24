package com.seebon.config.callback.impl;

import com.seebon.config.cache.EhcacheManager;
import com.seebon.config.callback.ConfigBeanHolder;
import com.seebon.config.callback.IConfigChangeCallBack;
import com.seebon.config.callback.IDelayConfigRefresh;
import com.seebon.config.spring.environment.SeeConfigEnvironment;
import com.seebon.config.thread.SeeConfigExecutorPool;
import com.seebon.config.thread.tasks.DelayConfigChangeTask;
import com.seebon.config.zookeeper.ConfigNode;

/**
 * 配置文件延迟生效回调
 *
 * @author xfz
 */
public class DelayConfigChangeCallBack implements IConfigChangeCallBack {

    @Override
    public void onConfigChanged(final ConfigNode configNode) {
        SeeConfigExecutorPool.getInstance().schedule(configNode, new DelayConfigChangeTask(configNode, new IDelayConfigRefresh() {
            @Override
            public void refreshDelayConfig(ConfigNode changedConfig) {
                //更新环境配置
                SeeConfigEnvironment.getInstance().refresh(configNode.getKeyName(), configNode.getKeyValue());
                //更新缓存配置
                EhcacheManager.getInstance().refresh(configNode.getKeyName(), configNode.getKeyValue());
                //更新@value属性值
                ConfigBeanHolder.refresh(configNode.getKeyName(), configNode.getKeyValue());
            }
        }));
    }
}
