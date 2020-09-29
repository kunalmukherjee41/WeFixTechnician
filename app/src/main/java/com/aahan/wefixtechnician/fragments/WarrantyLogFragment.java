package com.aahan.wefixtechnician.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.adapter.WarrantyLogHistoryAdapter;
import com.aahan.wefixtechnician.model.WarrantyLog;
import com.aahan.wefixtechnician.model.WarrantyLogResponse;
import com.aahan.wefixtechnician.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantyLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<WarrantyLog> warrantyLogList;
    RecyclerView recyclerView;
    TextView noRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_warranty_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        noRecord = view.findViewById(R.id.no_record);
        noRecord.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getLog();

    }

    private void getLog() {

        int tblDelearId = SharedPrefManager.getInstance(getActivity()).getTechnician().getTbl_technician_id();

        Call<WarrantyLogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getWarrantyCallLog(tblDelearId);

        call.enqueue(
                new Callback<WarrantyLogResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<WarrantyLogResponse> call, @NonNull Response<WarrantyLogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            warrantyLogList = response.body().getWarrantyLogList();
                            recyclerView.setItemViewCacheSize(warrantyLogList.size());
                            if (!warrantyLogList.isEmpty()) {
                                WarrantyLogHistoryAdapter adapter = new WarrantyLogHistoryAdapter(getActivity(), warrantyLogList);
                                adapter.setHasStableIds(true);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Logs Not Loaded! Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WarrantyLogResponse> call, @NonNull Throwable t) {
//                        progressBar.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    @Override
    public void onRefresh() {
        if (warrantyLogList != null)
            warrantyLogList.clear();
        getLog();
        if (warrantyLogList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

}