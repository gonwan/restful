package com.gonwan.restful.springboot.request;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public class GetRowCountRequest extends RequestBase {

    @NotEmpty
    private String apiName;

    @JsonSetter(nulls=Nulls.SKIP)
    @Min(0)
    private Integer apiVersion = Integer.valueOf(0);

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;

    @JsonSetter(nulls=Nulls.SKIP)
    private String[] columns = new String[]{ };

    @JsonSetter(nulls=Nulls.SKIP)
    private String conditions = "";

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Integer getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(Integer apiVersion) {
        this.apiVersion = apiVersion;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

}
