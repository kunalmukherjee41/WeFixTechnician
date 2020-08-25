package com.example.wefixtechnician.sendNotification;

import com.example.wefixtechnician.Api.Api;
import com.example.wefixtechnician.Api.RetrofitClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static final String BASE_URL = "https://fcm.googleapis.com/";
    private static Client mInstance;
    private Retrofit retrofit;

    private Client() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized Client getInstance() {
        if (mInstance == null) {
            mInstance = new Client();
        }
        return mInstance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

}
