package com.gonwan.restful.springboot;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonwan.restful.springboot.request.GetRowCountRequest;
import com.gonwan.restful.springboot.request.GetRowCountResponse;
import com.gonwan.restful.springboot.request.LoginRequest;
import com.gonwan.restful.springboot.request.ReloadRequest;
import com.gonwan.restful.springboot.request.ResponseBase;
import com.gonwan.restful.springboot.request.ResultTableResponse;
import com.gonwan.restful.springboot.request.RunApiRequest;
import com.gonwan.restful.springboot.service.LoginService;
import com.gonwan.restful.springboot.service.MainService;

@RestController
@RequestMapping(path = "/api")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private MainService mainService;

    /* See: https://blog.neospot.top/archives/11 */
    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping(path = "/login", produces = "application/json;charset=utf-8")
    public Callable<Object> login(@RequestBody @Valid LoginRequest request) {
        return new RestfulCallable(httpServletRequest, () -> {
            logger.info("Login: {}", request.getUsername());
            loginService.login(request);
            return ResponseBase.OK;
        });
    }

    @PostMapping(path = "/reload", produces = "application/json;charset=utf-8")
    public Callable<Object> reload(@RequestBody @Valid ReloadRequest request) {
        return new RestfulCallable(httpServletRequest, () -> {
            logger.info("Reload: {}", request.getDataType());
            mainService.reload(request);
            return ResponseBase.OK;
        });
    }

    @PostMapping(path = "/getrowcount", produces = "application/json;charset=utf-8")
    public Callable<Object> getRowCount(@RequestBody @Valid GetRowCountRequest request) {
        return new RestfulCallable(httpServletRequest, () -> {
            logger.info("GetRowCount: {}", request.getApiName());
            long res = mainService.getRowCount(request);
            GetRowCountResponse response = new GetRowCountResponse(ResponseBase.OK.getCode(), ResponseBase.OK.getMessage(), res);
            return response;
        });
    }

    @PostMapping(path = "/runapi", produces = "application/json;charset=utf-8")
    public Callable<Object> runApi(@RequestBody @Valid RunApiRequest request) {
        return new RestfulCallable(httpServletRequest, () -> {
            logger.info("RunApi: {}", request.getApiName());
            Object res = mainService.runApi(request);
            ResultTableResponse response = new ResultTableResponse(
                    ResponseBase.OK.getCode(), ResponseBase.OK.getMessage(), request.getStartPage(), request.getPageSize(), res);
            return response;
        });
    }

}
