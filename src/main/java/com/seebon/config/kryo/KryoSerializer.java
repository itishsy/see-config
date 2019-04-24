package com.seebon.config.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * kryo serializer
 * kryo 序列化工具类
 *
 * @author pengdeyi
 * @Date 2017年11月6日
 * @Version
 */
public final class KryoSerializer implements Serializer {

    private KryoPool pool = KryoPoolFactory.getKryoPoolInstance();

    public KryoSerializer(ClassLoader classLoader) {

    }

    @Override
    public ByteBuffer serialize(Object object) throws SerializerException {
        Kryo kryo = pool.borrow();
        Output out = new Output(new ByteArrayOutputStream());
        kryo.writeClassAndObject(out, object);
        ByteBuffer byteBuffer = ByteBuffer.wrap(out.getBuffer());
        out.close();
        pool.release(kryo);

        return byteBuffer;
    }

    @Override
    public Object read(ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        Kryo kryo = pool.borrow();
        Input input = new Input(new ByteBufferInputStream(byteBuffer));
        Object object = kryo.readClassAndObject(input);
        input.close();
        pool.release(kryo);

        return object;
    }

    @Override
    public boolean equals(Object object, ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        ByteBuffer serialize = serialize(object);
        return serialize.equals(byteBuffer);
    }
}
