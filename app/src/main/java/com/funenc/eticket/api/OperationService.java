package com.funenc.eticket.api;

import com.funenc.eticket.model.FelicityListResponse;
import com.funenc.eticket.model.HeadlineListResponse;
import com.funenc.eticket.model.MessageCountResponse;
import com.funenc.eticket.model.MessageListResponse;
import com.funenc.eticket.model.OrderListResponse;
import com.funenc.eticket.model.QiniuTokenResponse;
import com.funenc.eticket.model.StationListResponse;
import com.funenc.eticket.model.UserInfoResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @GET
    @Path("/auth/app/qiniu/token")
    @Produces(MediaType.APPLICATION_JSON)
    QiniuTokenResponse getQiniuToken(@HeaderParam("app-token") String appToken);

    @GET
    @Path("/users/me")
    @Produces(MediaType.APPLICATION_JSON)
    UserInfoResponse getUserInfo(@HeaderParam("app-token") String appToken);

    @PUT
    @Path("/users/me")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    UserInfoResponse changeUserInfo(@FormParam("name")String name, @FormParam("gender")String gender, @FormParam("email")String email, @FormParam("avatar")String avatar, @HeaderParam("app-token") String appToken);

    @GET
    @Path("/informations")
    @Produces(MediaType.APPLICATION_JSON)
    MessageListResponse getInformationList(@QueryParam("page")Integer page,
                                           @QueryParam("pageSize")Integer pageSize,
                                           @QueryParam("isMessage")Integer isMessage,
                                           @HeaderParam("app-token") String appToken);

    @GET
    @Path("/informations/unread-count")
    @Produces(MediaType.APPLICATION_JSON)
    MessageCountResponse getInformationUnreadCount(@HeaderParam("app-token") String appToken);

    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_JSON)
    OrderListResponse getOrderList(@HeaderParam("app-token")String token, @QueryParam("page")String page);

    @POST
    @Path("/orders/{orderId}/actualizar")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    OrderListResponse changeOrder(@FormParam("lineNo")String lineNo, @FormParam("stationNo")String stationNo, @FormParam("deviceNo")String deviceNo, @FormParam("deviceSerialNo")String deviceSerialNo, @FormParam("userCardNo")String userCardNo, @HeaderParam("app-token") String appToken, @PathParam("orderId")int orderId);

    @GET
    @Path("/stations")
    @Produces(MediaType.APPLICATION_JSON)
    StationListResponse getStationList();
}
