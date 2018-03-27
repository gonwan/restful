package com.gonwan.restful.springboot.request;

public class GetRowCountResponse extends ResponseBase {

    private Long dataRows;

    public GetRowCountResponse() {
    }

    public GetRowCountResponse(int code, String message, Long dataRows) {
        super(code, message);
        this.dataRows = dataRows;
    }

    public Long getDataRows() {
        return dataRows;
    }

    public void setDataRows(Long dataRows) {
        this.dataRows = dataRows;
    }

}
