package com.example.wefixtechnician.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<Logs> logsList;

    SwipeRefreshLayout mSwipeRefreshLayout;

    LogHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null)
                    adapter.getFilter().filter(newText);
                return false;
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getLog();
    }

    public void getLog() {

        int ref_technician_id = SharedPrefManager.getInstance(getActivity()).getTechnician().getTbl_technician_id();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForTechnician(ref_technician_id);

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
                            adapter = new LogHistoryAdapter(getActivity(), logs);
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
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