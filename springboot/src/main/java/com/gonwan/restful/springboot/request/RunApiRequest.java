package com.gonwan.restful.springboot.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RunApiRequest extends GetRowCountRequest {

    @NotNull
    @Min(1)
    private Integer startPage;

    @NotNull
    @Min(1)
    private Integer pageSize;

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
