package com.gonwan.restful.springboot.request;

import com.gonwan.restful.springboot.RestfulException;

public class ResponseBase {

    private int code;

    private String message;

    public static final ResponseBase OK = new ResponseBase(0, "OK");

    public ResponseBase() {
    }

    public ResponseBase(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBase(RestfulException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
