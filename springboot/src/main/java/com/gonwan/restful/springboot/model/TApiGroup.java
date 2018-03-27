package com.gonwan.restful.springboot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_api_group", schema = "restful_api", catalog = "")
public class TApiGroup {
    private Long groupId;
    private String groupName;
    private String comments;
    private String extra1;
    private String extra2;
    private String extra3;

    @Id
    @Column(name = "group_id", nullable = false)
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "group_name", nullable = true, length = 100)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
        TApiGroup tApiGroup = (TApiGroup) o;
        return Objects.equals(groupId, tApiGroup.groupId) &&
                Objects.equals(groupName, tApiGroup.groupName) &&
                Objects.equals(comments, tApiGroup.comments) &&
                Objects.equals(extra1, tApiGroup.extra1) &&
                Objects.equals(extra2, tApiGroup.extra2) &&
                Objects.equals(extra3, tApiGroup.extra3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupId, groupName, comments, extra1, extra2, extra3);
    }
}
