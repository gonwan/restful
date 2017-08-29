package com.gonwan.restful.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RequestMapping(path = "/api")
@RestController
public class ApiController {

    public static final Logger logger = LoggerFactory.getLogger(ApiController.class);

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
    public String getV2() {
        return "";
    }

    @RequestMapping(path = "/v3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getV3() {
        return "";
    }

}
