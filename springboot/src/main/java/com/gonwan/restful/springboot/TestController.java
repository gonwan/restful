package com.gonwan.restful.springboot;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

//@RestController
@RequestMapping(path = "/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private static final String url = "jdbc:mysql://172.16.66.120:3306/ss_product?user=artogrid&password=artogrid&useUnicode=true&characterEncoding=utf8";
    private static final String query = "select Bond_Key,Bond_Type,Bond_Subtype,Bond_ID,Interest_Start_Date,Maturity_Date,Coupon_Frequency,Currency,Interest_Basis,First_Coupon_Date,Coupon_Type,Redemption_Str,Coupon_Rate_Spread,FRN_Index_ID,Fixing_MA_Days,Fixing_Preceds,Fixing_Digit,Cap,Flr,Option_Style,Option_Type,Compensate_From,Compensate_Rate,Call_Str,Put_Str,Assign_Trans_Str,Issue_Price,Issue_Rate from bond where delflag = 0 limit 0,10000";
    private Connection conn = null;
    private JsonFactory factory = new JsonFactory();

    public TestController() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getString() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = factory.createGenerator(writer);
            generator.writeStartArray();
            while (rs.next()) {
                generator.writeStartObject();
                for (int i = 1; i <= columns; ++i) {
                    generator.writeFieldName(rs.getMetaData().getColumnName(i));
                    generator.writeObject(rs.getObject(i));
                }
                generator.writeEndObject();
            }
            generator.writeEndArray();
            generator.flush();
            return writer.toString();
        } catch (SQLException | IOException e) {
            logger.error("", e);
        }
        return "";
    }

}
