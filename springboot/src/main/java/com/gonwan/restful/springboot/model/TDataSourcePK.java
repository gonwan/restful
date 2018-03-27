package com.gonwan.restful.springboot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TDataSourcePK implements Serializable {
    private Long id;
    private Long databaseId;

    @Column(name = "id", nullable = false)
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "database_id", nullable = false)
    @Id
    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDataSourcePK that = (TDataSourcePK) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(databaseId, that.databaseId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, databaseId);
    }
}
