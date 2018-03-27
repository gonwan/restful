package com.gonwan.restful.springboot.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "t_user_date_count", schema = "restful_api", catalog = "")
@IdClass(TUserDateCountPK.class)
public class TUserDateCount {
    private String username;
    private Date rowDate;
    private Long rowCount;

    @Id
    @Column(name = "username", nullable = false, length = 32)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "row_date", nullable = false)
    public Date getRowDate() {
        return rowDate;
    }

    public void setRowDate(Date rowDate) {
        this.rowDate = rowDate;
    }

    @Basic
    @Column(name = "row_count", nullable = true)
    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUserDateCount that = (TUserDateCount) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(rowDate, that.rowDate) &&
                Objects.equals(rowCount, that.rowCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, rowDate, rowCount);
    }
}
