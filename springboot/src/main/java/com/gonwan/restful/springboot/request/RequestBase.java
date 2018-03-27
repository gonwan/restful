package com.gonwan.restful.springboot.request;

import javax.validation.constraints.NotEmpty;

public class RequestBase {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    public RequestBase() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void trimToEmpty() {

    }

}
