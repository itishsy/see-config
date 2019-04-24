package com.seebon.config.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * 创建kryo 序列化工厂,主要为了池化对象，加快效率，亦可这样去实现，
 * 直接去使用kryo去解析而不去池化，详细内容看kryo decoder和kryo encoder
 *
 * @author pengdeyi
 * @Date 2017年10月31日
 * @Version
 */
public final class KryoPoolFactory {

    private static volatile KryoPoolFactory poolFactory = null;

    /**
     * 私有化方法，保证对象不能通过构造方法实现
     */
    private KryoPoolFactory() {
    }

    private KryoPool pool = new KryoPool.Builder(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(Object.class);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        return kryo;
    }).build();

    /**
     * 为什么不使用直接静态方法去构造 因为需要constant 但是需要线程共享副本，需要最新的副本pool内容
     *
     * @return
     */
    public static KryoPool getKryoPoolInstance() {
        if (poolFactory == null) {
            synchronized (KryoPoolFactory.class) {
                if (poolFactory == null) {
                    poolFactory = new KryoPoolFactory();
                }
            }
        }
        return poolFactory.getPool();
    }

    /**
     * 不再对外提供方法来获取
     *
     * @return
     */
    private KryoPool getPool() {
        return pool;
    }
}
