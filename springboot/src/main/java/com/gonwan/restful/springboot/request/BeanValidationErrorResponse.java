package com.gonwan.restful.springboot.request;

import java.util.List;

import com.gonwan.restful.springboot.RestfulException.Predefined;

public class BeanValidationErrorResponse extends ResponseBase {

    private List<String> details;

    public BeanValidationErrorResponse() {
        super(Predefined.REQ_BEAN_VALIDATION_ERROR);
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

}
