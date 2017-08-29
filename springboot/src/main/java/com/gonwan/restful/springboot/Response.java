package com.gonwan.restful.springboot;

public class Response {

    private int code;
    private String message;
    public static final Response OK = new Response();

    public Response() {
        this(0, "OK");
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
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
