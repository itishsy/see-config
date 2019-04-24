package com.seebon.config.callback.impl;

import com.seebon.config.callback.IConfigChangeCallBack;
import com.seebon.config.zookeeper.ConfigNode;

/**
 * 配置文件重启生效回调
 *
 * @author xfz
 */
public class RestartChangeCallBack implements IConfigChangeCallBack {

    @Override
    public void onConfigChanged(ConfigNode configNode) {
        //TODO 暂时不实现
    }
}
