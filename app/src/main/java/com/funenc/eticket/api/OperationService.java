package com.funenc.eticket.api;

import com.funenc.eticket.model.FelicityListResponse;
import com.funenc.eticket.model.HeadlineListResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/api")
public interface OperationService {
    public static final String BASE_ADDR = "https://operator-app.funenc.com";

    @GET
    @Path("/headlines")
    @Produces(MediaType.APPLICATION_JSON)
    HeadlineListResponse getHeadlineList(@QueryParam("page") Integer page,
                                         @QueryParam("pageSize") Integer pageSize,
                                         @QueryParam("title") String title,
                                         @QueryParam("categoryId") Integer categoryId,
                                         @QueryParam("category") String category);

    @GET
    @Path("/felicities")
    @Produces(MediaType.APPLICATION_JSON)
    FelicityListResponse getFelicityList();
}
