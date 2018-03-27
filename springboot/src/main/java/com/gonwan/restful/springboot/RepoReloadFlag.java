package com.gonwan.restful.springboot;

import org.apache.commons.lang3.StringUtils;

public enum RepoReloadFlag {

    NONE("None", 0),
    USER("User", 1 << 0),
    API("Api", 1 << 1),
    API_GROUP("ApiGroup", 1 << 2),
    AUTHORITY("Authority", 1 << 3),
    DATA_SOURCE("DataSource", 1 << 4),
    USER_DATA_COUNT("UserDateCount", 1 << 5),
    ALL("All", (1 << 6) - 1);

    String name;
    int value;

    RepoReloadFlag(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public boolean contains(RepoReloadFlag flag) {
        return (value & flag.value) == flag.value;
    }

    @Override
    public String toString() {
        return name;
    }

    public static RepoReloadFlag parseString(String s) {
        for (RepoReloadFlag v : values()) {
            if (StringUtils.equalsIgnoreCase(v.toString(), s)) {
                return v;
            }
        }
        return NONE;
    }

}
