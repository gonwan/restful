package com.gonwan.restful.springboot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.gonwan.restful.springboot.request.BeanValidationErrorResponse;
import com.gonwan.restful.springboot.request.RequestBase;
import com.gonwan.restful.springboot.request.ResponseBase;

@RestControllerAdvice
class RestulRequestAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return RequestBase.class.isAssignableFrom(methodParameter.getParameterType())
                && AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        ((RequestBase)body).trimToEmpty();
        return body;
    }

}

@RestControllerAdvice
public class RestulResponseAdvice implements ResponseBodyAdvice<ResponseBase> {

    private static final Logger logger = LoggerFactory.getLogger(RestulResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ResponseBase.class.isAssignableFrom(returnType.getParameterType())
                && AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public ResponseBase beforeBodyWrite(ResponseBase body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HibernateException.class)
    public ResponseBase handleException(HibernateException e) {
        logger.warn("SQL query error", e);
        return new ResponseBase(RestfulException.Predefined.SYS_DATABASE_QUERY_ERROR);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RestfulException.class)
    public ResponseBase handleException(RestfulException e) {
        logger.warn("Got restful exception: " + e.getMessage());
        return new ResponseBase(e);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseBase handleException(Exception e) throws Exception {
        if (e.getClass().getName().startsWith("org.springframework")) {
            throw e;
        }
        /* see: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc */
/*
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
*/
        logger.error("Got unexpected exception", e);
        return new ResponseBase(RestfulException.Predefined.SYS_UNEXPECTED);
    }

    /* see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver */

    /* for @NotEmpty */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBase handleException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(x -> x.getField() + " - " + x.getDefaultMessage())
                .collect(Collectors.toList());
        BeanValidationErrorResponse response = new BeanValidationErrorResponse();
        response.setDetails(errors);
        return response;
    }

    /* JSON parse or @JsonFormat error */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseBase handleException(HttpMessageNotReadableException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMostSpecificCause().getMessage());
        BeanValidationErrorResponse response = new BeanValidationErrorResponse();
        response.setDetails(errors);
        return response;
    }

}
