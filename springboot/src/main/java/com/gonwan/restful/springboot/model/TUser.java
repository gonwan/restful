package com.gonwan.restful.springboot.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_user", schema = "restful_api", catalog = "")
public class TUser {
    private Long id;
    private Byte enabled;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String department;
    private String creator;
    private Timestamp createTime;
    private String comments;
    private String extra1;
    private String extra2;
    private String extra3;

    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    @Column(name = "username", nullable = false, length = 32)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 64)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "full_name", nullable = true, length = 100)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 32)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 64)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "department", nullable = true, length = 100)
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
        TUser tUser = (TUser) o;
        return Objects.equals(id, tUser.id) &&
                Objects.equals(enabled, tUser.enabled) &&
                Objects.equals(username, tUser.username) &&
                Objects.equals(password, tUser.password) &&
                Objects.equals(fullName, tUser.fullName) &&
                Objects.equals(phone, tUser.phone) &&
                Objects.equals(email, tUser.email) &&
                Objects.equals(department, tUser.department) &&
                Objects.equals(creator, tUser.creator) &&
                Objects.equals(createTime, tUser.createTime) &&
                Objects.equals(comments, tUser.comments) &&
                Objects.equals(extra1, tUser.extra1) &&
                Objects.equals(extra2, tUser.extra2) &&
                Objects.equals(extra3, tUser.extra3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, enabled, username, password, fullName, phone, email, department, creator, createTime, comments, extra1, extra2, extra3);
    }
}
