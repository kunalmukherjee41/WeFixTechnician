package com.aahan.wefixtechnician.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.model.Technician;
import com.aahan.wefixtechnician.model.TechnicianResponse;
import com.aahan.wefixtechnician.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView forgotPassword;
    ProgressDialog progressBar;
    String userID;

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

                    login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));

                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    if (TextUtils.isEmpty(txt_email)) {
                        Toast.makeText(LoginActivity.this, "Email required!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                        Toast.makeText(LoginActivity.this, "Enter a Valid Email Address!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();
                    } else if (TextUtils.isEmpty(txt_password)) {
                        Toast.makeText(LoginActivity.this, "Password Missing!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
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
//                            TechnicianResponse technicianResponse = response.body();
//                            assert technicianResponse != null;
//                            SharedPrefManager.getInstance(LoginActivity.this).saveTechnician(technicianResponse.getTechnician());
//                            Toast.makeText(LoginActivity.this, technicianResponse.getTechnician().getUsernmae() + "fdv", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            assert response.body() != null;
                            if (response.body().getMessage().equals("Login Successful")) {
                                Technician technician = response.body().getTechnician();
                                if (technician.getStatus().equals("ACTIVE")) {
                                    SharedPrefManager.getInstance(LoginActivity.this).saveTechnician(technician);
//                                    Toast.makeText(LoginActivity.this, technician.getTbl_technician_id() + "lbkjkv", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
                                    firebaseLogin(txt_email, txt_password);
                                } else {
                                    Snackbar snackBar = Snackbar.make(findViewById(R.id.id), "You Are No Longer in this Company!", Snackbar.LENGTH_LONG);
                                    snackBar.setAction("Action Message", v -> snackBar.dismiss());
                                    snackBar.show();
                                }
                            } else {
                                progressBar.dismiss();
                                Snackbar snackBar = Snackbar.make(findViewById(R.id.id), response.body().getMessage(), Snackbar.LENGTH_LONG);
                                snackBar.setAction("Action Message", v -> snackBar.dismiss());
                                snackBar.show();
                            }
                        }
                        login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.custom_btn2));
                        email.setText("");
                        password.setText("");
                        login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.custom_btn));
                    }

                    @Override
                    public void onFailure(Call<TechnicianResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        password.setText("");
                        progressBar.dismiss();
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

    private void firebaseLogin(String txt_email, String txt_password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                assert firebaseUser != null;
                                userID = firebaseUser.getUid();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", txt_email);
                                hashMap.put("id", userID);
                                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        progressBar.dismiss();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                });
                            } else {
                                auth.signInWithEmailAndPassword(txt_email, txt_password)
                                        .addOnCompleteListener(
                                                task12 -> {
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    progressBar.dismiss();
                                                }
                                        );
                            }
                        }
                );
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.pass_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.password_hide_asset);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.password_visible_asset);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}