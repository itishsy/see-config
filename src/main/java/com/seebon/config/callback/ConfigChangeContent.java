package com.seebon.config.callback;

import com.alibaba.fastjson.JSON;
import com.seebon.config.callback.impl.DelayConfigChangeCallBack;
import com.seebon.config.callback.impl.RealTimeConfigChangeCallBack;
import com.seebon.config.callback.impl.RestartChangeCallBack;
import com.seebon.config.enums.ChangeType;
import com.seebon.config.enums.EffectType;
import com.seebon.config.zookeeper.ConfigNode;
import org.apache.commons.lang3.StringUtils;

/**
 * 配置变更
 *
 * @author xufuzhou
 */
public class ConfigChangeContent {
    /**
     * 配置数据变更自定义回调
     */
    private IConfigChangeCallBack customCallBack = null;

    private static class SingletonHolder {
        private static final ConfigChangeContent INSTANCE = new ConfigChangeContent();
    }

    public static ConfigChangeContent getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onConfigChanged(String nodeData, ChangeType changeType) {
        ConfigNode configNode = JSON.parseObject(nodeData, ConfigNode.class);
        //处理配置更新
        if (ChangeType.REMOVED.equals(changeType)) {
            //节点删除时，清空值
            configNode.setKeyValue(StringUtils.EMPTY);
        }
        //获取默认回调
        IConfigChangeCallBack defaultCallBack = this.getDefaultCallBack(configNode);
        if (defaultCallBack != null) {
            defaultCallBack.onConfigChanged(configNode);
        }
        //获取自定义回调
        IConfigChangeCallBack customCallBack = this.getCustomCallBack();
        if (customCallBack != null) {
            customCallBack.onConfigChanged(configNode);
        }
    }

    /**
     * 获取默认回调
     *
     * @return
     */
    private IConfigChangeCallBack getDefaultCallBack(ConfigNode configNode) {
        IConfigChangeCallBack callBack = null;
        switch (EffectType.getByType(configNode.getEffectType())) {
            //实时生效
            case REALTIME:
                callBack = new RealTimeConfigChangeCallBack();
                break;
            // 延迟生效
            case DELAY:
                callBack = new DelayConfigChangeCallBack();
                break;
            // 重启生效
            case RESTART:
                callBack = new RestartChangeCallBack();
                break;
            default:
                callBack = null;
                break;
        }
        return callBack;
    }

    public IConfigChangeCallBack getCustomCallBack() {
        return customCallBack;
    }

    public void setCustomCallBack(IConfigChangeCallBack customCallBack) {
        this.customCallBack = customCallBack;
    }
}
