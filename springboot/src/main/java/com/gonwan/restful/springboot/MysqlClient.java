package com.gonwan.restful.springboot;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class MysqlClient {

    private static final Logger logger = LoggerFactory.getLogger(MysqlClient.class);
    //public static final String SQL = "select * from user";
    public static final String SQL = "select Bond_Key,Bond_Type,Bond_Subtype,Bond_ID,Interest_Start_Date,Maturity_Date,Coupon_Frequency,Currency,Interest_Basis,First_Coupon_Date,Coupon_Type,Redemption_Str,Coupon_Rate_Spread,FRN_Index_ID,Fixing_MA_Days,Fixing_Preceds,Fixing_Digit,Cap,Flr,Option_Style,Option_Type,Compensate_From,Compensate_Rate,Call_Str,Put_Str,Assign_Trans_Str,Issue_Price,Issue_Rate from bond where delflag = 0 limit 0,10000";

    /**
     * Here, we do not use JdbcTemplate, but a raw DataSource, since we will maintain the lifecycle of
     * connections and statements manually when returning Java 8 streams. Just keep consistency here.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Jackson provides good support for JDBC serialization within {@link ObjectMapper} by default.
     * @see {@link com.fasterxml.jackson.databind.ser.std.StdJdkSerializers}
     */
    private static JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

    public Pair<String, Long> executeToJson(String sql) {
        StringBuffer result = new StringBuffer();
        long size = 0;
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement();) {
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

    public Stream<Map> executeToStream(String sql) {
        try {
            return new MysqlStreamTemplate(dataSource).query(sql);
        } catch (SQLException e) {
            logger.warn("", e);
            return Stream.empty();
        }
    }

}
