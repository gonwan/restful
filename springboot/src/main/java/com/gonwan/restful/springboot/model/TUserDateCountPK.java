package com.gonwan.restful.springboot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class TUserDateCountPK implements Serializable {
    private String username;
    private Date rowDate;

    @Column(name = "username", nullable = false, length = 32)
    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "row_date", nullable = false)
    @Id
    public Date getRowDate() {
        return rowDate;
    }

    public void setRowDate(Date rowDate) {
        this.rowDate = rowDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUserDateCountPK that = (TUserDateCountPK) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(rowDate, that.rowDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, rowDate);
    }
}
