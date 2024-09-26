package com.technico.web.technico.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsManagement implements ContainerResponseFilter {

    /**
     * This filter method is responsible for adding Cross-Origin Resource
     * Sharing (CORS) headers to HTTP responses. It is invoked for every HTTP
     * request and response, allowing control over which origins can access the
     * resources. The CORS policy enables communication between different
     * domains.
     *
     * @param requestContext The context of the incoming HTTP request.
     * @param responseContext The context of the outgoing HTTP response where
     * the headers are added.
     * @throws IOException If an input or output exception occurs during the
     * request or response filtering process.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "username, password, X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers, crossdomain");
        responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}
