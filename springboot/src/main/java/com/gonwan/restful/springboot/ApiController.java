package com.gonwan.restful.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@RequestMapping(path = "/api")
@RestController
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private MysqlClient mysqlClient;

    @RequestMapping(path = "/v1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ApiResponse> getV1() {
        return () -> {
            String r = mysqlClient.executeToJson(MysqlClient.SQL).getLeft();
            return new ApiResponse(r);
        };
    }

    @RequestMapping(path = "/v2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ApiStreamResponse> getV2() {
        return () -> {
            Stream<Map> r = mysqlClient.executeToStream(MysqlClient.SQL);
            return new ApiStreamResponse(r);
        };
    }

    @RequestMapping(path = "/v3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getV3() {
        return "";
    }

}
