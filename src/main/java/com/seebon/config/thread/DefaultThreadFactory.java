package com.seebon.config.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ZookeeperThreadFactory
 *
 * @author xfz
 */
public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolId = new AtomicInteger();

    private final AtomicInteger nextId = new AtomicInteger();

    private final String prefix;

    private final boolean daemon;

    private final int priority;

    private final ThreadGroup threadGroup;

    public DefaultThreadFactory(String poolName) {
        this(poolName, false);
    }

    public DefaultThreadFactory(String poolName, boolean daemon) {
        this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
        this.daemon = daemon;
        this.priority = Thread.NORM_PRIORITY;
        this.threadGroup = System.getSecurityManager() == null ? Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, prefix + nextId.incrementAndGet(), 0);
        try {
            if (t.isDaemon() != daemon) {
                t.setDaemon(daemon);
            }
            if (t.getPriority() != priority) {
                t.setPriority(priority);
            }
        } catch (Exception ignored) {
            // Doesn't matter even if failed to set.
        }
        return t;
    }
}
