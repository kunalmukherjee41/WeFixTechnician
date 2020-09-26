package com.example.wefixtechnician.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.model.Category;
import com.example.wefixtechnician.model.Category1Response;
import com.example.wefixtechnician.model.Logs;
import com.example.wefixtechnician.model.My1Response;
import com.example.wefixtechnician.model.Service;
import com.example.wefixtechnician.model.Service1Response;
import com.example.wefixtechnician.model.UserResponse;
import com.example.wefixtechnician.sendNotification.Client;
import com.example.wefixtechnician.sendNotification.Data;
import com.example.wefixtechnician.sendNotification.MyResponse;
import com.example.wefixtechnician.sendNotification.NotificationSender;
import com.example.wefixtechnician.storage.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryDetailsActivity extends AppCompatActivity {

    private static final String TOPIC = "MyTOPIC";
    private TextView category;
    private TextView service;
    private Logs logs;

    private Button addParts;
    private Button showParts;
    private RelativeLayout layoutParts;
    private TextView partsDetails, partsAmount;

    private List<Service> serviceList;
    List<String> serviceItem;

    private ProgressDialog progressBar, progressDialog;

    private EditText title, message;
    private String userToken = null;
    private String userID;
    private Category cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Logs Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        logs = (Logs) i.getSerializableExtra("logs");

        progressBar = new ProgressDialog(this);
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        assert logs != null;
        getCategory(logs.getRefCatId());
        getService(logs.getRefServiceId(), logs.getRefCatId());

        //TextView
        category = findViewById(R.id.category);
        TextView name = findViewById(R.id.name);
        TextView address = findViewById(R.id.address);
        TextView pinCode = findViewById(R.id.pin);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView date = findViewById(R.id.date);
        TextView company = findViewById(R.id.company);
        service = findViewById(R.id.service);
        TextView amount = findViewById(R.id.amount);
        TextView problemDes = findViewById(R.id.problem_des);
        TextView status = findViewById(R.id.status);

        //notification
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        Button send = findViewById(R.id.send);

        //services buttons
        Button installation = findViewById(R.id.installation);
        Button onlyService = findViewById(R.id.service_only);
        Button onlyVisit = findViewById(R.id.visit);
        Button continueLog = findViewById(R.id.continue_log);

        //cardViews
        CardView cardViewCall = findViewById(R.id.layout_call);
        CardView cardViewMessage = findViewById(R.id.send_notification_card_view);
        CardView cardViewExtraParts = findViewById(R.id.card_view_extra_parts);
        if (logs.getRefDelear() > 0) {
            cardViewMessage.setVisibility(View.GONE);
        }
        if (logs.getCallLogStatus().equals("CANCEL") || logs.getCallLogStatus().equals("CLOSE") || logs.getCallLogStatus().equals("REJECT") || logs.getCallLogStatus().equals("COMPLETE")) {
            cardViewMessage.setVisibility(View.GONE);
            cardViewCall.setVisibility(View.GONE);
            cardViewExtraParts.setVisibility(View.GONE);
        }

        //Buttons
        addParts = findViewById(R.id.add_parts);
        Button submitParts = findViewById(R.id.submit_parts);
        showParts = findViewById(R.id.show_parts);
        Button cancel = findViewById(R.id.cancel);

        layoutParts = findViewById(R.id.layout_part);
        layoutParts.setVisibility(View.GONE);

        partsDetails = findViewById(R.id.parts_detail);
        partsAmount = findViewById(R.id.parts_amount);

        name.setText(logs.getClientName());
        address.setText(logs.getClientAddress());
        pinCode.setText(logs.getClientPin());
        email.setText(logs.getClientEmail());
        phone.setText(logs.getClientMb());
        date.setText(logs.getCallLogDate());
        company.setText(logs.getProductCompany());
        amount.setText(String.valueOf(logs.getAmount()));
        problemDes.setText(logs.getProblem());
        status.setText(logs.getCallLogStatus());

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        send.setOnClickListener(
                v -> getUserToken(logs.getClientEmail())
        );

        continueLog.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LogHistoryDetailsActivity.this, CompleteLogActivity.class);
                    if (!serviceList.isEmpty()) {
                        if (logs.getRefServiceId() == 37) {
                            intent.putExtra("service", 37);
                            intent.putExtra("name", "visit");
                        } else {
                            for (Service s : serviceList) {
                                if (s.getTbl_services_id() == logs.getRefServiceId()) {
                                    intent.putExtra("service", s);
                                    intent.putExtra("name", s.getTbl_services_name().toLowerCase());
                                }
                            }
                        }
                    }
                    if (cat != null) {
                        intent.putExtra("category", cat);
                    }
                    intent.putExtra("log", logs);
                    startActivity(intent);
                }
        );

        installation.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LogHistoryDetailsActivity.this, CompleteLogActivity.class);
                    if (!serviceList.isEmpty()) {
                        for (Service s : serviceList) {
                            if (s.getTbl_services_name().equals("Installation")) {
                                intent.putExtra("service", s);
                            }
                        }
                    }
                    if (cat != null) {
                        intent.putExtra("category", cat);
                    }
                    intent.putExtra("name", "installation");
                    intent.putExtra("log", logs);
                    startActivity(intent);
                }
        );

        onlyService.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LogHistoryDetailsActivity.this, CompleteLogActivity.class);
                    if (!serviceList.isEmpty()) {
                        for (Service s : serviceList) {
                            if (s.getTbl_services_name().equals("Service")) {
                                intent.putExtra("service", s);
                            }
                        }
                    }
                    if (cat != null) {
                        intent.putExtra("category", cat);
                    }
                    intent.putExtra("name", "service");
                    intent.putExtra("log", logs);
                    startActivity(intent);
                }
        );

        onlyVisit.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LogHistoryDetailsActivity.this, CompleteLogActivity.class);

                    intent.putExtra("service", 37);
                    if (cat != null) {
                        intent.putExtra("category", cat);
                    }
                    intent.putExtra("name", "visit");
                    intent.putExtra("log", logs);
                    startActivity(intent);
                }
        );

        addParts.setOnClickListener(
                v -> {
                    layoutParts.setVisibility(View.VISIBLE);
                    addParts.setVisibility(View.GONE);
                    showParts.setVisibility(View.GONE);
                }
        );

        showParts.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LogHistoryDetailsActivity.this, DisplayAllPartsActivity.class);
                    intent.putExtra("logs", logs);
                    startActivity(intent);
                }
        );

        cancel.setOnClickListener(
                v -> {
                    showParts.setVisibility(View.VISIBLE);
                    addParts.setVisibility(View.VISIBLE);
                    layoutParts.setVisibility(View.GONE);
                }
        );

        submitParts.setOnClickListener(
                v -> {

                    int txtCallLogID = logs.getCallLogId();
                    int txtTechnicianID = SharedPrefManager.getInstance(this).getTechnician().getTbl_technician_id();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    String currentDate = sdf.format(new Date());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf1.format(new Date());
                    String txtPartsDetail = partsDetails.getText().toString();
                    String txtPartsAmount = partsAmount.getText().toString();

                    if (TextUtils.isEmpty(txtPartsDetail)) {
                        Toast.makeText(LogHistoryDetailsActivity.this, "Enter the Parts Details", Toast.LENGTH_SHORT).show();
                        partsDetails.setFocusable(true);
                    } else if (TextUtils.isEmpty(txtPartsAmount)) {
                        Toast.makeText(LogHistoryDetailsActivity.this, "Enter the amount of the parts", Toast.LENGTH_SHORT).show();
                        partsAmount.setFocusable(true);
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Objects.requireNonNull(LogHistoryDetailsActivity.this));
                        builder1.setMessage("Are you want Complete the Call Log?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog1, id1) -> {
                                    ProgressDialog progressBar1 = new ProgressDialog(this);
                                    progressBar1.show();
                                    progressBar1.setContentView(R.layout.progress_dialog);
                                    Objects.requireNonNull(progressBar1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                                    Call<My1Response> call = RetrofitClient
                                            .getInstance()
                                            .getApi()
                                            .addParts(txtCallLogID, txtTechnicianID, txtPartsDetail, txtPartsAmount, currentDate, currentTime);

                                    call.enqueue(
                                            new Callback<My1Response>() {
                                                @Override
                                                public void onResponse(Call<My1Response> call, Response<My1Response> response) {
                                                    assert response.body() != null;
                                                    Toast.makeText(LogHistoryDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressBar1.dismiss();
                                                    partsAmount.setText("");
                                                    partsDetails.setText("");
                                                    showParts.setVisibility(View.VISIBLE);
                                                    addParts.setVisibility(View.VISIBLE);
                                                    layoutParts.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onFailure(Call<My1Response> call, Throwable t) {
                                                    progressBar1.dismiss();
                                                    Toast.makeText(LogHistoryDetailsActivity.this, "Parts Not Added", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    );
                                })
                                .setNegativeButton("No", (dialog1, id1) -> dialog1.cancel());
                        AlertDialog alert = builder1.create();
                        alert.show();

                    }
                }
        );

    }

    private void getService(int refServiceId, int refCatID) {

//        if(refServiceId != 37) {
        serviceItem = new ArrayList<>();
        final String[] serviceName = new String[1];

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getServiceByID(refCatID);

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            serviceList = response.body().getService();
                            for (Service s : serviceList) {
                                if (s.getTbl_services_id() == refServiceId) {
                                    serviceName[0] = s.getTbl_services_name();
                                }
//                                serviceItem.add(s.getTbl_services_name());
                            }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LogHistoryDetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, serviceItem);
                            service.setText(serviceName[0]);
                            if (logs.getRefServiceId() == 37) {
                                service.setText(R.string.only_visit);
                            }
                            progressBar.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        progressBar.dismiss();
                    }
                }
        );

//        }

    }

    private void getCategory(int refCatId) {

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(refCatId);

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            cat = response.body().getCategory();
                            category.setText(cat.getTbl_category_name());
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {

                    }
                }
        );
    }

    private void getUserToken(String clientEmail) {

        String txt_title = title.getText().toString().trim();
        String txt_message = message.getText().toString().trim();
        if (TextUtils.isEmpty(txt_title) || TextUtils.isEmpty(txt_message)) {
            Toast.makeText(LogHistoryDetailsActivity.this, "Enter the message and title", Toast.LENGTH_SHORT).show();
        } else {
            title.setText("");
            message.setText("");
            progressDialog = new ProgressDialog(LogHistoryDetailsActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

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

                                            sendNotifications(userToken, txt_title, txt_message);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LogHistoryDetailsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LogHistoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
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
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(LogHistoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}