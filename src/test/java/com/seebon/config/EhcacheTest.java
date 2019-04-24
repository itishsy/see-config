package com.seebon.config;

import com.seebon.config.cache.EhcacheManager;
import org.ehcache.Cache;
import org.junit.Test;

import java.io.IOException;

public class EhcacheTest {

    public EhcacheTest() {
        System.setProperty("see.config.client.appName", "module-system");
    }

    /**
     * 将Ehcache中的数据持久化
     */
    @Test
    public void test() throws IOException {
        Cache<String, Object> cache = EhcacheManager.getInstance().getCache();
        //循环遍历插入100条数据
        for (int i = 0; i < 100; i++) {
            cache.put(i + "", "value" + i);
        }
        //循环遍历输出
        cache.forEach(cc -> {
            System.out.println("主键=" + cc.getKey() + "，值=" + cc.getValue());
        });
        //释放资源并持久化到磁盘
        EhcacheManager.getInstance().close();
    }

    /**
     * 将Ehcache中的数据持久化
     */
    @Test
    public void testBean() throws IOException {
        Cache<String, Object> cache = EhcacheManager.getInstance().getCache();
        //循环遍历插入100条数据
        for (int i = 0; i < 100; i++) {
            Person test = new Person();
            test.setName("name" + i);
            test.setValue(i);
            cache.put(i + "", test);
        }
        //循环遍历输出
        cache.forEach(cc -> {
            Person test = (Person) cc.getValue();
            System.out.println("主键=" + cc.getKey() + "，值=" + test.getName());
        });
        //释放资源并持久化到磁盘
        EhcacheManager.getInstance().close();
    }

    /**
     * 从硬盘读取数据
     */
    @Test
    public void testCount() throws IOException {
        int count = 0;
        Cache<String, Object> cache = EhcacheManager.getInstance().getCache();
        //循环遍历输出
        cache.forEach(obj -> {
            if (obj.getValue() instanceof Person) {
                Person person = (Person) obj.getValue();
                System.out.println("主键=" + obj.getKey() + "，值 personName=" + person.getName() + "，personValue=" + person.getValue());
            }
        });
        System.out.println("信息的总条数为：" + count);

        EhcacheManager.getInstance().close();
    }

    /**
     * 清空持久化的信息
     */
    @Test
    public void testClean() throws IOException {
        Cache<String, Object> cache = EhcacheManager.getInstance().getCache();
        //清空信息
        cache.clear();
        //循环遍历输出
        cache.forEach(cc -> {
            System.out.println("cache,主键：{}，值：{}" + cc.getKey() + cc.getValue());
        });
        EhcacheManager.getInstance().close();
    }
}
