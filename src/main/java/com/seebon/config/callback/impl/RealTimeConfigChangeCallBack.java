package com.seebon.config.callback.impl;

import com.seebon.config.cache.EhcacheManager;
import com.seebon.config.callback.ConfigBeanHolder;
import com.seebon.config.callback.IConfigChangeCallBack;
import com.seebon.config.spring.environment.SeeConfigEnvironment;
import com.seebon.config.zookeeper.ConfigNode;

/**
 * 配置文件实时生效回调
 *
 * @author xfz
 */
public class RealTimeConfigChangeCallBack implements IConfigChangeCallBack {

    @Override
    public void onConfigChanged(ConfigNode configNode) {
        //更新环境配置
        SeeConfigEnvironment.getInstance().refresh(configNode.getKeyName(), configNode.getKeyValue());
        //更新缓存配置
        EhcacheManager.getInstance().refresh(configNode.getKeyName(), configNode.getKeyValue());
        //更新@value属性值
        ConfigBeanHolder.refresh(configNode.getKeyName(), configNode.getKeyValue());
    }
}
