package com.seebon.config.zookeeper;

/**
 * 配置节点
 *
 * @author xufuzhou
 */
public class ConfigNode {
    /**
     * 配置项
     */
    private String keyName;
    /**
     * 配置内容
     */
    private String keyValue;
    /**
     * key类型(1.mysql、2.mongo、3.activiti、4.redis、5.mail、6.rpc、7.log、8.other）
     */
    private Integer keyType;
    /**
     * 生效类型(1:实时生效，2:延迟生效，3:重启生效)
     */
    private Integer effectType;
    /**
     * 延迟时间（单位：分钟）
     */
    private Integer delayTime;
    /**
     * 配置状态(1.正常、2、失效)
     */
    private Integer status;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Integer getKeyType() {
        return keyType;
    }

    public void setKeyType(Integer keyType) {
        this.keyType = keyType;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
