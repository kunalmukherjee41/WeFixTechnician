package com.aahan.wefixtechnician.sendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0_CtXD8:APA91bEFBydZxLiLIDl-KVAOJbGcJ6uSaCBwPHX-OnpAYF5g6tQJI1_TcnOC_dMsHjEQe0mG_UkBrfE1-l-TGk_uf5mKL9WgpGH5SHZziRdB1JbvWyPrE76PpxBXhVXPI4y2gArGLHqw"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(
            @Body NotificationSender body
    );

}
