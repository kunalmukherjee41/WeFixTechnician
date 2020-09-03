package com.example.wefixtechnician;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.model.Category;
import com.example.wefixtechnician.model.Category1Response;
import com.example.wefixtechnician.model.Logs;
import com.example.wefixtechnician.model.UserResponse;
import com.example.wefixtechnician.sendNotification.Client;
import com.example.wefixtechnician.sendNotification.Data;
import com.example.wefixtechnician.sendNotification.MyResponse;
import com.example.wefixtechnician.sendNotification.NotificationSender;
import com.example.wefixtechnician.storage.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryDetailsActivity extends AppCompatActivity {

    private static final String TOPIC = "MyTOPIC";
    TextView date, log_type, name;
    TextView contact, address, category;
    TextView company, amount, status;
    Logs logs;
    ImageView image1;

    EditText title, message;
    Button send;
    private String userToken = null;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history_details);

        Intent i = getIntent();
        logs = (Logs) i.getSerializableExtra("logs");

//        getUserToken(logs.getClientEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Logs Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        date = findViewById(R.id.date);
        log_type = findViewById(R.id.log_type);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        category = findViewById(R.id.category);
        image1 = findViewById(R.id.image1);
        company = findViewById(R.id.company);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getUid());

        send.setOnClickListener(
                v -> {

//                    sendNotifications(userToken, title.getText().toString().trim(), message.getText().toString().trim());
                    getUserToken(logs.getClientEmail());
                }
        );

        getCategory(logs.getRefCatId());
    }

    private void getUserToken(String clientEmail) {

        Call<UserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getFirebaseId(clientEmail);

        call.enqueue(
                new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            userID = response.body().getUser().getId();
                            if (userID != null) {
//                                Toast.makeText(LogHistoryDetailsActivity.this, userID, Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("Tokens").child(userID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        userToken = dataSnapshot.getValue(String.class);
                                        sendNotifications(userToken, title.getText().toString().trim(), message.getText().toString().trim());
//                                        Toast.makeText(LogHistoryDetailsActivity.this, userToken, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            } else {
                                Toast.makeText(LogHistoryDetailsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {

                    }
                }
        );
    }

    private void getCategory(int refCatId) {

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(refCatId, "aa");

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            Category cat = response.body().getCategory();
                            category.setText(cat.getTbl_category_name());
                            date.setText(logs.getCallLogDate());
                            log_type.setText(logs.getCallLogType());
                            name.setText(logs.getClientName());
                            String txt_contact = logs.getClientEmail() + "\n" + logs.getClientMb();
                            contact.setText(txt_contact);
                            address.setText(logs.getClientAddress());
                            amount.setText(String.valueOf(logs.getAmount()));
                            status.setText(logs.getCallLogStatus());
//                            Glide.with(LogHistoryDetailsActivity.this).load("http://wefix.sitdoxford.org/product/" + cat.getTbl_category_image()).into(image1);
                            Picasso.get().load("http://wefix.sitdoxford.org/product/" + cat.getTbl_category_image()).into(image1);
                            company.setText(logs.getProductCompany());
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {

                    }
                }
        );
    }

    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, userToken);
        Call<MyResponse> call = Client
                .getInstance()
                .getApiService()
                .sendNotification(sender);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().success != 1) {
                        Toast.makeText(LogHistoryDetailsActivity.this, "Failed! Try Again", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LogHistoryDetailsActivity.this, "Successfully send", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(LogHistoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}