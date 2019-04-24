package com.seebon.config;

import com.seebon.config.callback.IConfigChangeCallBack;
import com.seebon.config.zookeeper.ConfigNode;

/**
 * 自定义回调
 */
public class MyCallBackDemo implements IConfigChangeCallBack {

    @Override
    public void onConfigChanged(ConfigNode configNode) {

        System.out.println("KeyName=" + configNode.getKeyName());
    }

}
