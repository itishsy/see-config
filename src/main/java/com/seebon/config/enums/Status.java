package com.seebon.config.enums;

/**
 * 配置状态(1.正常、2、失效)
 *
 * @author xufz
 */
public enum Status {
    NORMAL(1, "正常"),
    INVALID(2, "失效");

    private int type;
    private String name;

    Status(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}

