package com.example.wefixtechnician;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.model.TechnicianResponse;
import com.example.wefixtechnician.storage.SharedPrefManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView forgotPassword;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgot_password);

        login.setOnClickListener(
                v -> {
                    progressBar = new ProgressDialog(LoginActivity.this);
                    progressBar.show();
                    progressBar.setContentView(R.layout.progress_dialog);
                    Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                    login.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_btn2));

                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    if (TextUtils.isEmpty(txt_email)) {
                        Toast.makeText(LoginActivity.this, "Email required!", Toast.LENGTH_SHORT).show();
                        login.setBackground(getResources().getDrawable(R.drawable.custom_btn));
                        progressBar.dismiss();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                        Toast.makeText(LoginActivity.this, "Enter a Valid Email Address!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_btn));
                        progressBar.dismiss();
                    } else if (TextUtils.isEmpty(txt_password)) {
                        Toast.makeText(LoginActivity.this, "Password Missing!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_btn));
                        progressBar.dismiss();
                    } else {
                        userlogin(txt_email, txt_password);
                    }
                }
        );
    }

    private void userlogin(String txt_email, String txt_password) {

        Call<TechnicianResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .technicianLogin(txt_email, txt_password);

        call.enqueue(
                new Callback<TechnicianResponse>() {
                    @Override
                    public void onResponse(Call<TechnicianResponse> call, Response<TechnicianResponse> response) {
                        if (response.isSuccessful()) {
                            TechnicianResponse technicianResponse = response.body();
                            assert technicianResponse != null;
                            SharedPrefManager.getInstance(LoginActivity.this).saveTechnician(technicianResponse.getTechnician());
//                            Toast.makeText(LoginActivity.this, technicianResponse.getTechnician().getTbl_technician_id(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.custom_btn2));
                        email.setText("");
                        password.setText("");
                        login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.custom_btn));
                        progressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<TechnicianResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.custom_btn));
                        password.setText("");
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}