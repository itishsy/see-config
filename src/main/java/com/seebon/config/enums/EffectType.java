package com.seebon.config.enums;

/**
 * 生效类型(1:实时生效，2:延迟生效，3:重启生效)
 *
 * @author xufz
 */
public enum EffectType {
    REALTIME(1, "实时生效"),
    DELAY(2, "延迟生效"),
    RESTART(3, "重启生效");

    private int type;
    private String name;

    EffectType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getNameByType(int type) {
        for (EffectType mt : values()) {
            if (mt.getType() == type) {
                return mt.getName();
            }
        }
        return null;
    }

    public static EffectType getByType(int type) {
        for (EffectType mt : values()) {
            if (mt.getType() == type) {
                return mt;
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

