package com.example.wefixtechnician;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.fragments.AllLogFragment;
import com.example.wefixtechnician.fragments.CallLogFragment;
import com.example.wefixtechnician.sendNotification.Token;
import com.example.wefixtechnician.storage.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new CallLogFragment(), "Open Call Logs");
        viewPagerAdapter.addFragment(new AllLogFragment(), "All Call Log");
//        viewPagerAdapter.addFragment(new CancelledLogFragment(), "Cancelled Log");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        Toast.makeText(MainActivity.this, FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();

        String firebaseID = FirebaseAuth.getInstance().getUid();
        String username = SharedPrefManager.getInstance(this).getTechnician().getUsernmae();
        Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(MainActivity.this, firebaseID, Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Log.d("MainActivity123", "Field");
//                            Toast.makeText(MainActivity.this, "firebaseID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        UpdateToken();
    }

    public void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("token", refreshToken);
//        hashMap.put("email", SharedPrefManager.getInstance(this).getTechnician().getUsernmae());
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "dsc", Toast.LENGTH_SHORT);
                            }
                        }
                );
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

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

}