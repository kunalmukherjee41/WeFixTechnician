package com.example.wefixtechnician.Api;

import com.example.wefixtechnician.model.Category1Response;
import com.example.wefixtechnician.model.Company1Response;
import com.example.wefixtechnician.model.LogResponse;
import com.example.wefixtechnician.model.My1Response;
import com.example.wefixtechnician.model.PartsResponse;
import com.example.wefixtechnician.model.Service1Response;
import com.example.wefixtechnician.model.TechnicianResponse;
import com.example.wefixtechnician.model.UserResponse;
import com.example.wefixtechnician.model.WarrantyLogResponse;

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
    @PUT("getuserfirebaseid")
    Call<UserResponse> getFirebaseId(
            @Field("username") String username
    );

    @FormUrlEncoded
    @PUT("updatetechnician")
    Call<ResponseBody> updateFirebaseID(
            @Field("firebaseID") String firebaseID,
            @Field("username") String username
    );

    @FormUrlEncoded
    @PUT("updatetechnicianpassword")
    Call<My1Response> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("username") String username
    );

    @FormUrlEncoded
    @PUT("getcompanybyid/{tbl_company_id}")
    Call<Company1Response> getCompanyById(
            @Path("tbl_company_id") int tbl_company_id,
            @Field("app") String app
    );

    @FormUrlEncoded
    @PUT("getwarrantycalllogfortechnician/{technician_id}")
    Call<WarrantyLogResponse> getWarrantyCallLog(
            @Path("technician_id") int technician_id,
            @Field("app") String app
    );

    @FormUrlEncoded
    @PUT("getservice/{id}")
    Call<Service1Response> getServiceByID(
            @Path("id") int id,
            @Field("app") String app
    );

    @FormUrlEncoded
    @PUT("completecalllog/{call_log_id}")
    Call<ResponseBody> completeCallLog(
            @Path("call_log_id") int call_log_id,
            @Field("ref_service_id") int ref_service_id,
            @Field("call_log_status") String call_log_status,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("addparts")
    Call<My1Response> addParts(
            @Field("ref_log_id") int ref_log_id,
            @Field("ref_technician_id") int ref_technician_id,
            @Field("partdes") String partdes,
            @Field("amount") String amount,
            @Field("entry_date") String date,
            @Field("entry_time") String time
    );

    @FormUrlEncoded
    @PUT("getparts/{ref_log_id}")
    Call<PartsResponse> getParts(
            @Path("ref_log_id") int ref_log_id,
            @Field("app") String app
    );

}
