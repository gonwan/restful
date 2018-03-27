package com.gonwan.restful.springboot.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_authority", schema = "restful_api", catalog = "")
public class TAuthority {
    private Long id;
    private Long userId;
    private String readApiGroupIds;
    private String writeApiGroupIds;
    private String creator;
    private Timestamp createTime;
    private String comments;
    private String extra1;
    private String extra2;
    private String extra3;
    private Long maxRowOnce;
    private Long maxRowDaily;

    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "read_api_group_ids", nullable = true, length = 200)
    public String getReadApiGroupIds() {
        return readApiGroupIds;
    }

    public void setReadApiGroupIds(String readApiGroupIds) {
        this.readApiGroupIds = readApiGroupIds;
    }

    @Basic
    @Column(name = "write_api_group_ids", nullable = true, length = 200)
    public String getWriteApiGroupIds() {
        return writeApiGroupIds;
    }

    public void setWriteApiGroupIds(String writeApiGroupIds) {
        this.writeApiGroupIds = writeApiGroupIds;
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

    @Basic
    @Column(name = "max_row_once", nullable = true)
    public Long getMaxRowOnce() {
        return maxRowOnce;
    }

    public void setMaxRowOnce(Long maxRowOnce) {
        this.maxRowOnce = maxRowOnce;
    }

    @Basic
    @Column(name = "max_row_daily", nullable = true)
    public Long getMaxRowDaily() {
        return maxRowDaily;
    }

    public void setMaxRowDaily(Long maxRowDaily) {
        this.maxRowDaily = maxRowDaily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TAuthority that = (TAuthority) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(readApiGroupIds, that.readApiGroupIds) &&
                Objects.equals(writeApiGroupIds, that.writeApiGroupIds) &&
                Objects.equals(creator, that.creator) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(extra1, that.extra1) &&
                Objects.equals(extra2, that.extra2) &&
                Objects.equals(extra3, that.extra3) &&
                Objects.equals(maxRowOnce, that.maxRowOnce) &&
                Objects.equals(maxRowDaily, that.maxRowDaily);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, readApiGroupIds, writeApiGroupIds, creator, createTime, comments, extra1, extra2, extra3, maxRowOnce, maxRowDaily);
    }
}
