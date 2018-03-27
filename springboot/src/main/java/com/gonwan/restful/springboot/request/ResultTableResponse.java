package com.gonwan.restful.springboot.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultTableResponse extends ResponseBase {

    private Integer startPage;

    private Integer pageSize;

    private Object resultTable;

    public ResultTableResponse() {
    }

    public ResultTableResponse(int code, String message, Object resultTable) {
        super(code, message);
        this.resultTable = resultTable;
    }

    public ResultTableResponse(int code, String message, int startPage, int pageSize, Object resultTable) {
        super(code, message);
        this.startPage = startPage;
        this.pageSize = pageSize;
        this.resultTable = resultTable;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Object getResultTable() {
        return resultTable;
    }

    public void setResultTable(Object resultTable) {
        this.resultTable = resultTable;
    }

}
