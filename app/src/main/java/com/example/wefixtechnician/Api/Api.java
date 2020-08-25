package com.example.wefixtechnician.Api;

import com.example.wefixtechnician.model.Category1Response;
import com.example.wefixtechnician.model.LogResponse;
import com.example.wefixtechnician.model.TechnicianResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("technicianlogin")
    Call<TechnicianResponse> technicianLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @PUT("getcalllogfortechnician/{ref_technician_id}")
    Call<LogResponse> getCallLogForTechnician(
            @Path("ref_technician_id") int ref_technician_id,
            @Field("app") String app
    );

    @FormUrlEncoded
    @PUT("getcategorybyid/{tbl_category_id}")
    Call<Category1Response> getCategoryByID(
            @Path("tbl_category_id") int tbl_category_id,
            @Field("app") String app
    );

    @FormUrlEncoded
    @PUT("/updatetechnician")
    Call<ResponseBody> updateFirebaseID(
            @Field("firebaseID") String firebaseID,
            @Field("username") String username
    );

}
