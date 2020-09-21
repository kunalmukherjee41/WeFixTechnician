package com.example.wefixtechnician.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.adapter.LogHistoryAdapter;
import com.example.wefixtechnician.model.LogResponse;
import com.example.wefixtechnician.model.Logs;
import com.example.wefixtechnician.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<Logs> logsList;

    ProgressDialog progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getLog();
    }

    public void getLog() {

//        progressBar = new ProgressDialog(getActivity());
//        progressBar.show();
//        progressBar.setContentView(R.layout.progress_dialog);
//        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        int ref_technician_id = SharedPrefManager.getInstance(getActivity()).getTechnician().getTbl_technician_id();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForTechnician(ref_technician_id, "app");

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            logsList = response.body().getLog();
//                            Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (!logs1.getCallLogStatus().equals("CANCEL") && !logs1.getCallLogStatus().equals("CLOSE") && !logs1.getCallLogStatus().equals("REJECT") && !logs1.getCallLogStatus().equals("COMPLETE")) {
                                    logs.add(logs1);
                                }
                            }
                            recyclerView.setItemViewCacheSize(logs.size());
                            LogHistoryAdapter adapter = new LogHistoryAdapter(getActivity(), logs);
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
//                            progressBar.dismiss();
                        } else {
//                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//                        progressBar.dismiss();

                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        getLog();

        if (logsList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

}