package com.aahan.wefixtechnician.Api;

import com.aahan.wefixtechnician.model.Category1Response;
import com.aahan.wefixtechnician.model.Company1Response;
import com.aahan.wefixtechnician.model.LogResponse;
import com.aahan.wefixtechnician.model.My1Response;
import com.aahan.wefixtechnician.model.PartsResponse;
import com.aahan.wefixtechnician.model.Service1Response;
import com.aahan.wefixtechnician.model.TechnicianResponse;
import com.aahan.wefixtechnician.model.UserResponse;
import com.aahan.wefixtechnician.model.WarrantyLogResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @PUT("getcalllogfortechnician/{ref_technician_id}")
    Call<LogResponse> getCallLogForTechnician(@Path("ref_technician_id") int ref_technician_id);

    @PUT("getcategorybyid/{tbl_category_id}")
    Call<Category1Response> getCategoryByID(@Path("tbl_category_id") int tbl_category_id);

    @FormUrlEncoded
    @PUT("getuserfirebaseid")
    Call<UserResponse> getFirebaseId(@Field("username") String username);

    @PUT("getcompanybyid/{tbl_company_id}")
    Call<Company1Response> getCompanyById(@Path("tbl_company_id") int tbl_company_id);

    @PUT("getwarrantycalllogfortechnician/{technician_id}")
    Call<WarrantyLogResponse> getWarrantyCallLog(@Path("technician_id") int technician_id);

    @PUT("getservice/{id}")
    Call<Service1Response> getServiceByID(@Path("id") int id);

    @DELETE("deletepartsbyid/{parts_id}")
    Call<My1Response> deletePartsByID(@Path("parts_id") int parts_id);

    @PUT("getparts/{ref_log_id}")
    Call<PartsResponse> getParts(@Path("ref_log_id") int ref_log_id);

    @FormUrlEncoded
    @PUT("updateremarks/{call_log_id}")
    Call<My1Response> updateRemarks(
            @Path("call_log_id") int call_log_id,
            @Field("remarks") String remarks
    );

    @FormUrlEncoded
    @POST("technicianlogin")
    Call<TechnicianResponse> technicianLogin(
            @Field("username") String username,
            @Field("password") String password
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
    @PUT("completecalllog/{call_log_id}")
    Call<ResponseBody> completeCallLog(
            @Path("call_log_id") int call_log_id,
            @Field("ref_service_id") int ref_service_id,
            @Field("call_log_status") String call_log_status,
            @Field("amount") String amount,
            @Field("close_date") String close_date
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

}
