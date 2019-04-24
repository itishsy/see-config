package com.seebon.config.thread.tasks;

import com.seebon.config.callback.IDelayConfigRefresh;
import com.seebon.config.zookeeper.ConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件延迟生效刷新任务
 *
 * @author xfz
 * @Date 2018年07月03日
 * @Version
 */
public class DelayConfigChangeTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DelayConfigChangeTask.class);

    /**
     * 配置数据
     */
    private ConfigNode configNode;

    /**
     * 延迟刷新
     */
    private IDelayConfigRefresh delayConfigRefresh;

    public DelayConfigChangeTask(ConfigNode configNode, IDelayConfigRefresh delayConfigRefresh) {
        this.configNode = configNode;
        this.delayConfigRefresh = delayConfigRefresh;
    }

    @Override
    public void run() {
        logger.info("刷新延迟生效的配置数据=====开始");

        delayConfigRefresh.refreshDelayConfig(configNode);

        logger.info("刷新延迟生效的配置数据=====结束");
    }
}
