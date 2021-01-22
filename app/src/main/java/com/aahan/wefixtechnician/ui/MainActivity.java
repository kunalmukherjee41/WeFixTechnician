package com.aahan.wefixtechnician.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.fragments.AllLogFragment;
import com.aahan.wefixtechnician.fragments.CallLogFragment;
import com.aahan.wefixtechnician.fragments.PaymentFragment;
import com.aahan.wefixtechnician.model.My1Response;
import com.aahan.wefixtechnician.model.Technician;
import com.aahan.wefixtechnician.model.TechnicianResponse;
import com.aahan.wefixtechnician.sendNotification.Token;
import com.aahan.wefixtechnician.storage.SharedPrefManager;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        saveToken(token);
                        updateToken(token);
//                        Toast.makeText(this, token + "\nag", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(this, task.getException() + "\nag", Toast.LENGTH_SHORT).show();
                    }
                });

        userExist();
        updateApk();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        String txt_name = "Welcome " + SharedPrefManager.getInstance(this).getTechnician().getTechnician_name();
        TextView name = findViewById(R.id.name);
        name.setText(txt_name);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

//        viewPagerAdapter.addFragment(new LogFragment(), "All Logs");
        viewPagerAdapter.addFragment(new CallLogFragment(), "Open Logs");
        viewPagerAdapter.addFragment(new AllLogFragment(), "Close Logs");
        viewPagerAdapter.addFragment(new PaymentFragment(), "Payment");
        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        String firebaseID = FirebaseAuth.getInstance().getUid();
        String username = SharedPrefManager.getInstance(this).getTechnician().getUsernmae();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateFirebaseID(firebaseID, username);

        call.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (!SharedPrefManager.getInstance(MainActivity.this).isLoggedFirebase()) {
                                SharedPrefManager.getInstance(MainActivity.this).saveFirebaseId(1);
//                                Toast.makeText(MainActivity.this, firebaseID, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void updateToken(String token) {

        String username = SharedPrefManager.getInstance(this).getTechnician().getUsernmae();

        Call<My1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateFirebaseToken(token, username);

        call.enqueue(new Callback<My1Response>() {
            @Override
            public void onResponse(Call<My1Response> call, Response<My1Response> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(MainActivity.this, response.body().getMessage() + " Some Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<My1Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToken(String token1) {
//        String email = mAuth.getCurrentUser().getEmail();
//        Token token1 = new Token(token);
//
//        FirebaseDatabase.getInstance().getReference("Tokens").child(mAuth.getUid()).setValue(token1)
//                .addOnCompleteListener(
//                        task -> {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(this, "Token Saved", Toast.LENGTH_SHORT);
//                            }
//                        }
//                );

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);

        if (firebaseUser.getUid() != null) {
            assert firebaseUser != null;
            FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "erf", Toast.LENGTH_SHORT);
                                }
                            }
                    );
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                fetchContact("users", newText);
//                return false;
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefManager.getInstance(this).clear();
                Intent intent2 = new Intent(this, LoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finish();
                return true;

            case R.id.change_password:
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                return true;
        }

        return false;
    }

    private void userExist() {
        String email = SharedPrefManager.getInstance(this).getTechnician().getUsernmae();

        Call<TechnicianResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTechnicianByEmail(email, "abc");

        call.enqueue(
                new Callback<TechnicianResponse>() {
                    @Override
                    public void onResponse(Call<TechnicianResponse> call, Response<TechnicianResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Technician t = response.body().getTechnician();

                            assert t != null;
                            if (TextUtils.isEmpty(t.getPassword()) || !t.getPassword().equals(SharedPrefManager.getInstance(MainActivity.this).getTechnician().getPassword()) || t.getStatus().equals("INACTIVE")) {
                                if (!TextUtils.isEmpty(t.getPassword())) {
                                    if (!t.getPassword().equals(SharedPrefManager.getInstance(MainActivity.this).getTechnician().getPassword())) {
                                        Toast.makeText(MainActivity.this, "Password Change!", Toast.LENGTH_SHORT).show();
                                    } else if (t.getStatus().equals("INACTIVE")) {
                                        Toast.makeText(MainActivity.this, "You are not a active Technician!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Logout\tTechnician Not Exist.", Toast.LENGTH_SHORT).show();
                                }
                                SharedPrefManager.getInstance(MainActivity.this).clear();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TechnicianResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateApk() {
        String apkUrl = "https://wefixservice.in/android/wefixtechnician.json";

        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON(apkUrl)
                .setDisplay(Display.NOTIFICATION)
                .setDisplay(Display.DIALOG)
                .start();
    }


}