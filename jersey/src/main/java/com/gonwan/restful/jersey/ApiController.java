package com.gonwan.restful.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/api")
public class ApiController {

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getString(@Suspended AsyncResponse asyncResponse) {
        executor.submit(() -> {
            String r = MysqlClient.executeToJson(MysqlClient.SQL).getLeft();
            ApiResponse res = new ApiResponse(r);
            asyncResponse.resume(res);
        });
    }

}
