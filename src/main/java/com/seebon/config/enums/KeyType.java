package com.seebon.config.enums;

/**
 * key类型(1.mysql、2.mongo、3.activiti、4.redis、5.mail、6.其他）
 *
 * @author xufz
 */
public enum KeyType {
    MYSQL(1, "mysql"),
    MONGO(2, "mongo"),
    ACTIVITI(3, "activiti"),
    REDIS(4, "redis"),
    MAIL(5, "mail"),
    RPC(6, "rpc"),
    LOG(7, "log"),
    OTHER(8, "other");

    private int type;
    private String name;

    KeyType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getByType(int type) {
        for (KeyType mt : values()) {
            if (mt.getType() == type) {
                return mt.getName();
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
