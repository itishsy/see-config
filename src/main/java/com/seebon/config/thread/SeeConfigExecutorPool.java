package com.seebon.config.thread;

import com.seebon.config.constants.Constants;
import com.seebon.config.thread.tasks.DelayConfigChangeTask;
import com.seebon.config.zookeeper.ConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 配置线程池
 *
 * @author xfz
 */
public class SeeConfigExecutorPool implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SeeConfigExecutorPool.class);

    private ScheduledExecutorService scheduler;

    private SeeConfigExecutorPool() {
        scheduler = new ScheduledThreadPoolExecutor(Constants.POOL_SIZE, new DefaultThreadFactory(Constants.SEE_CONFIG + "-scheduled", Boolean.TRUE));
    }

    private static class SingletonInstance {
        private static final SeeConfigExecutorPool SINGLETON = new SeeConfigExecutorPool();
    }

    public static SeeConfigExecutorPool getInstance() {
        return SeeConfigExecutorPool.SingletonInstance.SINGLETON;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * 延时N分钟执行
     *
     * @param configNode
     * @param task
     */
    public void schedule(ConfigNode configNode, DelayConfigChangeTask task) {
        long delay = configNode.getDelayTime().longValue();

        logger.info("配置文件延迟 " + delay + " 分钟生效.");

        this.getScheduler().schedule(task, delay, TimeUnit.MINUTES);
    }

    @Override
    public void destroy() {
        if (getScheduler() != null) {
            System.out.println("SeeConfigExecutorPool ======================》 shutdown.....");
            getScheduler().shutdown();
        }
    }
}
