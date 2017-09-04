package com.gonwan.restful.jersey;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.dbcp2.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlClient {

    private static final Logger logger = LoggerFactory.getLogger(MysqlClient.class);
    //public static final String URL = "jdbc:mysql://172.16.66.120:3306/mysql?user=artogrid&password=artogrid&useUnicode=true&characterEncoding=utf8";
    //public static final String SQL = "select * from user";
    public static final String URL = "jdbc:mysql://172.16.66.120:3306/ss_product?user=artogrid&password=artogrid&useUnicode=true&characterEncoding=utf8";
    public static final String SQL = "select Bond_Key,Bond_Type,Bond_Subtype,Bond_ID,Interest_Start_Date,Maturity_Date,Coupon_Frequency,Currency,Interest_Basis,First_Coupon_Date,Coupon_Type,Redemption_Str,Coupon_Rate_Spread,FRN_Index_ID,Fixing_MA_Days,Fixing_Preceds,Fixing_Digit,Cap,Flr,Option_Style,Option_Type,Compensate_From,Compensate_Rate,Call_Str,Put_Str,Assign_Trans_Str,Issue_Price,Issue_Rate from bond where delflag = 0 limit 0,10000";

    private static DataSource dataSource;
    /**
     * Jackson provides good support for JDBC serialization within {@link ObjectMapper} by default.
     * @see {@link com.fasterxml.jackson.databind.ser.std.StdJdkSerializers}
     */
    private static JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
    private static CsvFactory csvFactory = new CsvFactory(new CsvMapper());

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        }
    }

    private static DataSource getDataSource() {
        if (dataSource == null) {
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(URL, null);
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
            poolableConnectionFactory.setValidationQuery("select 1");
            GenericObjectPoolConfig connectionPoolConfig = new GenericObjectPoolConfig();
            connectionPoolConfig.setTestOnBorrow(true);
            //connectionPoolConfig.setMaxIdle(8);
            //connectionPoolConfig.setMinIdle(0);
            connectionPoolConfig.setTimeBetweenEvictionRunsMillis(1800 * 1000);
            connectionPoolConfig.setMinEvictableIdleTimeMillis(3600 * 1000);
            connectionPoolConfig.setNumTestsPerEvictionRun(30);
            ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, connectionPoolConfig);
            poolableConnectionFactory.setPool(connectionPool);
            dataSource = new PoolingDataSource<>(connectionPool);
        }
        return dataSource;
    }

    public static Pair<String, Long> executeToJson(String sql) {
        StringBuffer result = new StringBuffer();
        long size = 0;
        try (Connection conn = getDataSource().getConnection(); Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(sql);
            int columns = rs.getMetaData().getColumnCount();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = jsonFactory.createGenerator(writer);
            generator.writeStartArray();
            while (rs.next()) {
                generator.writeStartObject();
                for (int i = 1; i <= columns; ++i) {
                    generator.writeFieldName(rs.getMetaData().getColumnLabel(i));
                    Object obj = rs.getObject(i);
                    generator.writeObject(obj);
                }
                generator.writeEndObject();
                size++;
            }
            generator.writeEndArray();
            generator.flush();
            result = writer.getBuffer();
        } catch (com.mysql.jdbc.exceptions.MySQLSyntaxErrorException | com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            logger.warn("", e);
        } catch (SQLException | IOException e) {
            logger.warn("", e);
        } catch (Exception e) {
            logger.warn("", e);
        }
        return Pair.of(result.toString(), size);
    }

    public static Pair<String, Long> executeToCsv(String sql) {
        StringBuffer result = new StringBuffer();
        long size = 0;
        try (Connection conn = getDataSource().getConnection(); Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(sql);
            CsvSchema.Builder builder = CsvSchema.builder();
            int columns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; ++i) {
                builder.addColumn(rs.getMetaData().getColumnLabel(i));
            }
            CsvSchema schema = builder.setUseHeader(true).build();
            StringWriter writer = new StringWriter();
            CsvGenerator generator = csvFactory.createGenerator(writer);
            generator.setSchema(schema);
            while (rs.next()) {
                generator.writeStartArray();
                for (int i = 1; i <= columns; ++i) {
                    Object obj = rs.getObject(i);
                    generator.writeObject(obj == null ? "" : obj);
                }
                generator.writeEndArray();
                size++;
            }
            generator.flush();
            result = writer.getBuffer();
        } catch (com.mysql.jdbc.exceptions.MySQLSyntaxErrorException | com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            logger.warn("", e);
        } catch (SQLException | IOException e) {
            logger.warn("", e);
        } catch (Exception e) {
            logger.warn("", e);
        }
        return Pair.of(result.toString(), size);
    }

}
