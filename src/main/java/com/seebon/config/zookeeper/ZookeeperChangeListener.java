package com.seebon.config.zookeeper;

import com.seebon.config.callback.ConfigChangeContent;
import com.seebon.config.enums.ChangeType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zookeeper 节点变更监听
 *
 * @author xfz
 */
public class ZookeeperChangeListener implements PathChildrenCacheListener {
    private final static Logger logger = LoggerFactory.getLogger(ZookeeperChangeListener.class);

    private void childAdded(String path, String pathData) {
        logger.info("节点添加通知.path=" + path + ",nodeData=" + pathData);

        this.childChanged(pathData, ChangeType.ADD);
    }

    private void childRemoved(String path, String pathData) {
        logger.info("节点删除通知.path=" + path + ",nodeData=" + pathData);

        this.childChanged(pathData, ChangeType.REMOVED);
    }

    private void childUpdated(String path, String pathData) {
        logger.info("节点更新通知.path=" + path + ",nodeData=" + pathData);

        this.childChanged(pathData, ChangeType.UPDATE);
    }

    private void connectionSuspended() {
        logger.info("ZookeeperChangeListener =====> connectionSuspended()");
    }

    private void connectionReconnected() {
        logger.info("ZookeeperChangeListener =====> connectionReconnected()");
    }

    private void connectionLost() {
        logger.info("ZookeeperChangeListener =====> connectionLost()");
    }

    private void initialized() {
        logger.info("ZookeeperChangeListener =====> initialized()");
    }

    @Override
    public void childEvent(CuratorFramework curator, PathChildrenCacheEvent cacheEvent) throws Exception {
        if (cacheEvent.getData() == null) {
            return;
        }
        String path = cacheEvent.getData().getPath();
        String pathData = new String(cacheEvent.getData().getData());
        switch (cacheEvent.getType()) {
            case CHILD_ADDED:
                childAdded(path, pathData);
                break;
            case CHILD_REMOVED:
                childRemoved(path, pathData);
                break;
            case CHILD_UPDATED:
                childUpdated(path, pathData);
                break;
            case CONNECTION_SUSPENDED:
                connectionSuspended();
                break;
            case CONNECTION_RECONNECTED:
                connectionReconnected();
                break;
            case CONNECTION_LOST:
                connectionLost();
                break;
            case INITIALIZED:
                initialized();
                break;
            default:
                break;
        }
    }

    private void childChanged(String currentData, ChangeType changeType) {
        ConfigChangeContent.getInstance().onConfigChanged(currentData, changeType);
    }
}
