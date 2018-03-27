package com.gonwan.restful.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * TODO:
 * - password encryption.
 * - zipkin integration.
 * - dropwizard integration.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfiguration.class, args);
    }

}
