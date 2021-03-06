package com.aahan.wefixtechnician.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.model.My1Response;
import com.aahan.wefixtechnician.storage.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPass, rePassword, newPassword;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        currentPass = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        rePassword = findViewById(R.id.r_password);
        change = findViewById(R.id.change);

        change.setOnClickListener(
                v -> {
                    change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));
                    String txt_current_pass = currentPass.getText().toString();
                    String txt_new_pass = newPassword.getText().toString();
                    String txt_re_pass = rePassword.getText().toString();
                    if (TextUtils.isEmpty(txt_current_pass)) {
                        currentPass.setFocusable(true);
                        Toast.makeText(ChangePasswordActivity.this, "Enter the Current Password.", Toast.LENGTH_SHORT).show();
                        change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                    } else if (TextUtils.isEmpty(txt_new_pass)) {
                        newPassword.setFocusable(true);
                        Toast.makeText(ChangePasswordActivity.this, "Enter the New Password.", Toast.LENGTH_SHORT).show();
                        change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                    } else if (TextUtils.isEmpty(txt_re_pass)) {
                        newPassword.setFocusable(true);
                        Toast.makeText(ChangePasswordActivity.this, "Re Password Enter New Password", Toast.LENGTH_SHORT).show();
                        change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                    } else if (!txt_new_pass.equals(txt_re_pass)) {
                        Toast.makeText(ChangePasswordActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                        newPassword.setText("");
                        rePassword.setText("");
                        change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                    } else {
                        changePassword(txt_current_pass, txt_new_pass);
                    }
                }
        );

    }

    private void changePassword(String txt_currentPass, String txt_newPassword) {

        String username = SharedPrefManager.getInstance(this).getTechnician().getUsernmae();

        Call<My1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .updatePassword(txt_currentPass, txt_newPassword, username);

        call.enqueue(
                new Callback<My1Response>() {
                    @Override
                    public void onResponse(Call<My1Response> call, Response<My1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            String message = response.body().getMessage();
                            updateFirebasePassword(username, txt_currentPass, txt_newPassword);
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        rePassword.setText("");
                        currentPass.setText("");
                        newPassword.setText("");
                        change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                    }

                    @Override
                    public void onFailure(Call<My1Response> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        change.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                    }
                }
        );
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.curr_pass_btn) {
            if (currentPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.password_hide_asset);
                //Show Password
                currentPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.password_visible_asset);
                //Hide Password
                currentPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void ShowHidePass2(View view) {

        if (view.getId() == R.id.new_pass_btn) {
            if (newPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.password_hide_asset);
                //Show Password
                newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.password_visible_asset);
                //Hide Password
                newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void ShowHidePass3(View view) {

        if (view.getId() == R.id.re_pass_btn) {
            if (rePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.password_hide_asset);
                //Show Password
                rePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.password_visible_asset);
                //Hide Password
                rePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    void updateFirebasePassword(String username, String txt_password, String txt_new_password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(username, txt_password);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(txt_new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "Password updated");
                                } else {
//                                    Log.d(TAG, "Error password not updated")
                                }
                            }
                        });
                    } else {
//                        Log.d(TAG, "Error auth failed")
                    }
                });
    }

}