package com.example.wefixtechnician;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.model.Category;
import com.example.wefixtechnician.model.Category1Response;
import com.example.wefixtechnician.model.Logs;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryDetailsActivity extends AppCompatActivity {

    TextView date, log_type, name;
    TextView contact, address, category;
    TextView company, amount, status;
    Logs logs;
    ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Logs Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        logs = (Logs) i.getSerializableExtra("logs");

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

        getCategory(logs.getRefCatId());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}