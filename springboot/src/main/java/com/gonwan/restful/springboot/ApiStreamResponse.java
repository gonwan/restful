package com.gonwan.restful.springboot;

import java.util.Map;
import java.util.stream.Stream;

public class ApiStreamResponse extends Response {

    /* requires jackson-datatype-jdk8 2.9.0 */
    private Stream<Map> result;

    public ApiStreamResponse(Stream<Map> result) {
        this.result = result;
    }

    public Stream<Map> getResult() {
        return result;
    }

    public void setResult(Stream<Map> result) {
        this.result = result;
    }

}
