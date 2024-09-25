package com.technico.web.technico.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("jakartaee10")
public class JakartaEE10Resource {
    
    @PermitAll
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping(){
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
}
