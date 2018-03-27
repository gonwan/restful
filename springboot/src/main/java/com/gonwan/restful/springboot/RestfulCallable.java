package com.gonwan.restful.springboot;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestfulCallable implements Callable<Object> {

    private static final Logger logger = LoggerFactory.getLogger(RestfulCallable.class);

    private HttpServletRequest request;
    private Callable<Object> callable;

    public RestfulCallable(HttpServletRequest req, Callable<Object> callable) {
        this.request = req;
        this.callable = callable;
    }

    @Override
    public Object call() throws Exception {
        String requestInfo = String.format("%s(%s)", request.getRequestURI(), request.getRemoteAddr());
        logger.info("Requesting: {}", requestInfo);
        long t0 = System.currentTimeMillis();
        try {
            Object res = callable.call();
            logger.info("Request succeeded: {}", requestInfo);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            logger.info("Request {} finished in {} ms", requestInfo, (System.currentTimeMillis()-t0));
        }
    }

}
