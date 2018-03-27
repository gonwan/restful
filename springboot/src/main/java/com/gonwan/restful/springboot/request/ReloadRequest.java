package com.gonwan.restful.springboot.request;

import javax.validation.constraints.NotEmpty;

public class ReloadRequest extends RequestBase {

    @NotEmpty
    private String dataType;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
