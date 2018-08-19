package com.example.eticket.api;

import com.example.eticket.model.JourneyListResponse;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/api")
public interface SubwayService {
    public static final String BASE_ADDR = "https://scan-app.funenc.com/";

    @GET
    @Path("/records")
    @Produces(MediaType.APPLICATION_JSON)
    JourneyListResponse getJourneyList(@HeaderParam("app-token")String token, @QueryParam("page")String page);
}
