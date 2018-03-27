package com.gonwan.restful.springboot.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_api", schema = "restful_api", catalog = "")
public class TApi {
    private Long apiId;
    private Byte enabled;
    private String apiName;
    private String apiGroups;
    private Integer version;
    private Long datasourceId;
    private Long datasourceDatabaseId;
    private String logicSql;
    private String mandatoryCondition;
    private Byte whereAvailable;
    private String dateColumn;
    private String creator;
    private Timestamp createTime;
    private String comments;
    private String extra1;
    private String extra2;
    private String extra3;

    @Id
    @Column(name = "api_id", nullable = false)
    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    @Basic
    @Column(name = "enabled", nullable = false)
    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "api_name", nullable = true, length = 100)
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    @Basic
    @Column(name = "api_groups", nullable = true, length = 50)
    public String getApiGroups() {
        return apiGroups;
    }

    public void setApiGroups(String apiGroups) {
        this.apiGroups = apiGroups;
    }

    @Basic
    @Column(name = "version", nullable = true)
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Basic
    @Column(name = "datasource_id", nullable = false)
    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    @Basic
    @Column(name = "datasource_database_id", nullable = false)
    public Long getDatasourceDatabaseId() {
        return datasourceDatabaseId;
    }

    public void setDatasourceDatabaseId(Long datasourceDatabaseId) {
        this.datasourceDatabaseId = datasourceDatabaseId;
    }

    @Basic
    @Column(name = "logic_sql", nullable = true, length = -1)
    public String getLogicSql() {
        return logicSql;
    }

    public void setLogicSql(String logicSql) {
        this.logicSql = logicSql;
    }

    @Basic
    @Column(name = "mandatory_condition", nullable = true, length = -1)
    public String getMandatoryCondition() {
        return mandatoryCondition;
    }

    public void setMandatoryCondition(String mandatoryCondition) {
        this.mandatoryCondition = mandatoryCondition;
    }

    @Basic
    @Column(name = "where_available", nullable = true)
    public Byte getWhereAvailable() {
        return whereAvailable;
    }

    public void setWhereAvailable(Byte whereAvailable) {
        this.whereAvailable = whereAvailable;
    }

    @Basic
    @Column(name = "date_column", nullable = true, length = 200)
    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    @Basic
    @Column(name = "creator", nullable = true, length = 64)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "comments", nullable = true, length = 1000)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "extra1", nullable = true, length = 200)
    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    @Basic
    @Column(name = "extra2", nullable = true, length = 200)
    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    @Basic
    @Column(name = "extra3", nullable = true, length = 200)
    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TApi tApi = (TApi) o;
        return Objects.equals(apiId, tApi.apiId) &&
                Objects.equals(enabled, tApi.enabled) &&
                Objects.equals(apiName, tApi.apiName) &&
                Objects.equals(apiGroups, tApi.apiGroups) &&
                Objects.equals(version, tApi.version) &&
                Objects.equals(datasourceId, tApi.datasourceId) &&
                Objects.equals(datasourceDatabaseId, tApi.datasourceDatabaseId) &&
                Objects.equals(logicSql, tApi.logicSql) &&
                Objects.equals(mandatoryCondition, tApi.mandatoryCondition) &&
                Objects.equals(whereAvailable, tApi.whereAvailable) &&
                Objects.equals(dateColumn, tApi.dateColumn) &&
                Objects.equals(creator, tApi.creator) &&
                Objects.equals(createTime, tApi.createTime) &&
                Objects.equals(comments, tApi.comments) &&
                Objects.equals(extra1, tApi.extra1) &&
                Objects.equals(extra2, tApi.extra2) &&
                Objects.equals(extra3, tApi.extra3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(apiId, enabled, apiName, apiGroups, version, datasourceId, datasourceDatabaseId, logicSql, mandatoryCondition, whereAvailable, dateColumn, creator, createTime, comments, extra1, extra2, extra3);
    }
}
