package com.aahan.wefixtechnician.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.model.LogResponse;
import com.aahan.wefixtechnician.model.Logs;
import com.aahan.wefixtechnician.model.SumAllLogResponse;
import com.aahan.wefixtechnician.storage.SharedPrefManager;
import com.aahan.wefixtechnician.ui.PaidCallLogActivity;
import com.aahan.wefixtechnician.ui.PaymentLogActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Logs> logsList, onlyVisitLogsList = null, completeLogsList;
    private ProgressBar progressBar;
    private int refTechnicianId, txtPaidLog = -1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView totalLogs, onlyVisit, completeLog;
    private TextView paidLog, dueLog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refTechnicianId = SharedPrefManager.getInstance(getActivity()).getTechnician().getTbl_technician_id();

        mSwipeRefreshLayout = view.findViewById(R.id.layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        totalLogs = view.findViewById(R.id.total_log);
        onlyVisit = view.findViewById(R.id.only_visit);
        completeLog = view.findViewById(R.id.complete_log);
        paidLog = view.findViewById(R.id.total_paid_log);
        dueLog = view.findViewById(R.id.due_call_log);

        progressBar = view.findViewById(R.id.progress_bar);

        CardView totalLogCardView = view.findViewById(R.id.total_log_card_view);
        CardView onlyVisitCardView = view.findViewById(R.id.only_visit_card_view);
        CardView completeLogCardView = view.findViewById(R.id.complete_log_card_view);
        CardView totalCompleteCallLog = view.findViewById(R.id.total_call_log_card_view);

        totalLogCardView.setOnClickListener(
                v -> {
                    if (logsList != null) {
                        Intent intent = new Intent(getActivity(), PaymentLogActivity.class);
                        intent.putExtra("logs", (Serializable) logsList);
                        intent.putExtra("message", "All Logs");
                        startActivity(intent);
                    }
                }
        );

        onlyVisitCardView.setOnClickListener(
                v -> {
                    if (!onlyVisitLogsList.isEmpty()) {
                        Intent intent = new Intent(getActivity(), PaymentLogActivity.class);
                        intent.putExtra("message", "Only Visit");
                        intent.putExtra("logs", (Serializable) onlyVisitLogsList);
                        startActivity(intent);
                    }
                }
        );

        completeLogCardView.setOnClickListener(
                v -> {
                    if (txtPaidLog > 0)
                        startActivity(new Intent(getActivity(), PaidCallLogActivity.class));
                }
        );

        totalCompleteCallLog.setOnClickListener(
                v -> {
//                    List<Logs> paidCallLogList = new ArrayList<>();
//                    for (Logs l : completeLogsList) {
//                        if (l.getRefServiceId() != 37)
//                            paidCallLogList.add(l);
//                    }
                    if (!completeLogsList.isEmpty()) {
                        Intent intent = new Intent(getActivity(), PaymentLogActivity.class);
                        intent.putExtra("message", "Complete Logs");
                        intent.putExtra("logs", (Serializable) completeLogsList);
                        startActivity(intent);
                    }
                }
        );

        getLog();

    }

    private void getSumPaidLog(int completeLogList) {

        Call<SumAllLogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .sumAllPaidLog(refTechnicianId);

        call.enqueue(
                new Callback<SumAllLogResponse>() {
                    @Override
                    public void onResponse(Call<SumAllLogResponse> call, Response<SumAllLogResponse> response) {
                        assert response.body() != null;
                        if (!response.body().isError()) {
                            txtPaidLog = response.body().getTotal();
                            paidLog.setText(String.valueOf(txtPaidLog));
                            dueLog.setText(String.valueOf(completeLogList - txtPaidLog));

                        }
                    }

                    @Override
                    public void onFailure(Call<SumAllLogResponse> call, Throwable t) {

                    }
                }
        );
    }

    public void getLog() {

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForTechnician(refTechnicianId);

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            int onlyVisitLog = 0;
                            int completeLogList = 0;
                            int totalLogList = 0;
                            if (logsList != null) {
                                logsList.clear();
                            }
                            logsList = response.body().getLog();

                            completeLogsList = new ArrayList<>();
                            onlyVisitLogsList = new ArrayList<>();
                            for (Logs logs : logsList) {
                                totalLogList += 1;
                                if (logs.getRefServiceId() == 37) {
                                    onlyVisitLog += 1;
                                    onlyVisitLogsList.add(logs);
                                }
                                if (logs.getCallLogStatus().toUpperCase().equals("COMPLETE")) {
                                    completeLogList += 1;
                                    completeLogsList.add(logs);
                                }
                            }
//                            completeLogList -= onlyVisitLog;
                            getSumPaidLog(completeLogList);
                            totalLogs.setText(String.valueOf(totalLogList));
                            onlyVisit.setText(String.valueOf(onlyVisitLog));
                            completeLog.setText(String.valueOf(completeLogList));
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        if (logsList != null)
            logsList.clear();
        getLog();
        if (logsList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

}