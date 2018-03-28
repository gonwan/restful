package com.gonwan.restful.springboot;

@SuppressWarnings("serial")
public class RestfulException extends RuntimeException {

    private int code;

    public RestfulException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static class Predefined {

        public static final RestfulException SYS_UNEXPECTED                 = new RestfulException(90001, "Unexpected error");
        public static final RestfulException SYS_NO_CONNECTION_STRING       = new RestfulException(90002, "No connection string");
        public static final RestfulException SYS_MULTIPLE_CONNECTION_STRING = new RestfulException(90003, "Duplicate connection string");
        public static final RestfulException SYS_INVALID_CONNECTION_STRING  = new RestfulException(90004, "Invalid connection string");
        public static final RestfulException SYS_DATASOURCE_INVALID_SQL     = new RestfulException(90005, "Datasource invalid SQL");
        public static final RestfulException SYS_DATABASE_QUERY_ERROR       = new RestfulException(90006, "Database query error");

        public static final RestfulException REQ_INVALID_JSON               = new RestfulException(10001, "Invalid JSON request");
        public static final RestfulException REQ_BEAN_VALIDATION_ERROR      = new RestfulException(10002, "Parameter validation error");
        public static final RestfulException REQ_DATE_RANGE_ERROR           = new RestfulException(10003, "Date range error");
        public static final RestfulException REQ_INVALID_QUERY_CONDITION    = new RestfulException(10004, "Invalid query condition");

        public static final RestfulException USER_NOT_EXIST                 = new RestfulException(20001, "User not exist");
        public static final RestfulException USER_WRONG_PASSWORD            = new RestfulException(20002, "User wrong password");
        public static final RestfulException USER_NOT_AUTHORIZED            = new RestfulException(20003, "User not authorized");
        public static final RestfulException USER_API_NOT_AUTHORIZED        = new RestfulException(20004, "User API not authorized");
        public static final RestfulException USER_NO_API_VERSION            = new RestfulException(20005, "User no API version");
        public static final RestfulException USER_MULTIPLE_API_VERSION      = new RestfulException(20006, "User multiple API version");
        public static final RestfulException USER_EXCEED_MAX_QUERY_QUOTA    = new RestfulException(20007, "User exceeds max query quota");
        public static final RestfulException USER_EXCEED_MAX_DAILY_QUOTA    = new RestfulException(20008, "User exceeds max daily quota");

    }

}
