package com.gonwan.restful.springboot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_data_source", schema = "restful_api", catalog = "")
@IdClass(TDataSourcePK.class)
public class TDataSource {
    private Long id;
    private Long databaseId;
    private String databaseName;
    private String connectionString;
    private Byte dialect;
    private String readUsername;
    private String readPassword;
    private String writeUsername;
    private String writePassword;
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

    @Id
    @Column(name = "database_id", nullable = false)
    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    @Basic
    @Column(name = "database_name", nullable = true, length = 128)
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Basic
    @Column(name = "connection_string", nullable = true, length = 256)
    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Basic
    @Column(name = "dialect", nullable = true)
    public Byte getDialect() {
        return dialect;
    }

    public void setDialect(Byte dialect) {
        this.dialect = dialect;
    }

    @Basic
    @Column(name = "read_username", nullable = true, length = 32)
    public String getReadUsername() {
        return readUsername;
    }

    public void setReadUsername(String readUsername) {
        this.readUsername = readUsername;
    }

    @Basic
    @Column(name = "read_password", nullable = true, length = 64)
    public String getReadPassword() {
        return readPassword;
    }

    public void setReadPassword(String readPassword) {
        this.readPassword = readPassword;
    }

    @Basic
    @Column(name = "write_username", nullable = true, length = 32)
    public String getWriteUsername() {
        return writeUsername;
    }

    public void setWriteUsername(String writeUsername) {
        this.writeUsername = writeUsername;
    }

    @Basic
    @Column(name = "write_password", nullable = true, length = 64)
    public String getWritePassword() {
        return writePassword;
    }

    public void setWritePassword(String writePassword) {
        this.writePassword = writePassword;
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
        TDataSource that = (TDataSource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(databaseId, that.databaseId) &&
                Objects.equals(databaseName, that.databaseName) &&
                Objects.equals(connectionString, that.connectionString) &&
                Objects.equals(dialect, that.dialect) &&
                Objects.equals(readUsername, that.readUsername) &&
                Objects.equals(readPassword, that.readPassword) &&
                Objects.equals(writeUsername, that.writeUsername) &&
                Objects.equals(writePassword, that.writePassword) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(extra1, that.extra1) &&
                Objects.equals(extra2, that.extra2) &&
                Objects.equals(extra3, that.extra3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, databaseId, databaseName, connectionString, dialect, readUsername, readPassword, writeUsername, writePassword, comments, extra1, extra2, extra3);
    }
}
