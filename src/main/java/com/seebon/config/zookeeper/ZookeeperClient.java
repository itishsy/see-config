package com.seebon.config.zookeeper;

import com.seebon.config.constants.Constants;
import com.seebon.config.thread.DefaultThreadFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * zookeeper client
 *
 * @author xfz
 */
public class ZookeeperClient {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private Object lock = new Object();

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile CuratorFramework client;

    /**
     * 监听线程池
     */
    private ExecutorService watcherPool;

    private ZookeeperClient() {
        watcherPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE, Constants.CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new DefaultThreadFactory(Constants.SEE_CONFIG + "-zookeeper"));
    }

    private static class SingletonInstance {
        private static final ZookeeperClient SINGLETON = new ZookeeperClient();
    }

    public static ZookeeperClient getInstance() {
        return ZookeeperClient.SingletonInstance.SINGLETON;
    }

    public ZookeeperClient init(String address) {
        synchronized (lock) {
            if (client == null) {
                this.connect(address);
            }
            lock.notifyAll();
        }
        return this;
    }

    private void connect(String address) {
        if (this.client != null) {
            return;
        }
        this.client = CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(Constants.SESSION_TIMEOUTMS)
                .connectionTimeoutMs(Constants.CONNECTION_TIMEOUTMS)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.getConnectionStateListenable().addListener((CuratorFramework client, ConnectionState newState) -> {
            if (ConnectionState.CONNECTED.equals(newState)) {
                latch.countDown();
            }
            logger.info("注册中心连接状态：" + newState.isConnected());
        });
        this.client.start();
    }

    private CuratorFramework getClient() {
        try {
            latch.await(Constants.CONNECT_SYNC_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * 注册节点
     *
     * @param path     节点名称
     * @param nodeType 永久节点 0 临时节点 1
     */
    public void register(String path, int nodeType, String data) throws Exception {
        CreateMode mode = nodeType == 0 ? CreateMode.PERSISTENT : CreateMode.EPHEMERAL;
        if (this.getClient().checkExists().forPath(path) == null) {
            this.getClient().create().creatingParentsIfNeeded().withMode(mode).forPath(path, data.getBytes());
        }
    }

    /**
     * 绑定子节点变化事件器
     *
     * @param path
     * @param listener
     */
    public void addChildrenNodeListener(String path, ZookeeperChangeListener listener) throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(this.getClient(), path, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(listener, watcherPool);
    }

    /**
     * 获取当前节点下的所有的子节点
     *
     * @param path 父节点路径
     * @return 返回当前节点下所有的子节点数据
     */
    public List<String> getChildrenPath(String path) throws Exception {
        return this.getClient().getChildren().forPath(path);
    }

    /**
     * 获取节点数据
     *
     * @param path
     * @return
     */
    public byte[] getNodeData(String path) throws Exception {
        return this.getClient().getData().forPath(path);
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (this.getClient() != null) {
            this.getClient().close();
        }
    }
}
