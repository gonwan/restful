package com.gonwan.restful.springboot;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class ApiResponse extends Response {

    @JsonRawValue
    private String result;

    public ApiResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
