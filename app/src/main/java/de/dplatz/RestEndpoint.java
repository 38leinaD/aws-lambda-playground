package de.dplatz;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class RestEndpoint {
    
    @GET
    public Response helloWorld() {
        System.out.println("Hello 123!!!!");

        return Response.ok("Hello from server @" + System.currentTimeMillis(), "text/plain").build();
    }
}

