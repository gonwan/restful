package com.gonwan.restful.jersey;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final URI BASE_URI = URI.create("http://localhost:5050/");

    public static void main(String[] args) {
        try {
            final ResourceConfig resourceConfig = new ResourceConfig(ApiController.class);
            final HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig, false);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> httpServer.shutdownNow()));
            httpServer.start();
            logger.info("com.gonwan.restful.jersey.Application started.");
            logger.info("Try out {}", BASE_URI);
            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            logger.error("", e);
        }
    }

}
