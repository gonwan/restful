package com.gonwan.restful.springboot;

public enum PubSubChannel {

    REPO_RELOAD("RepoReload");

    String name;

    PubSubChannel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
