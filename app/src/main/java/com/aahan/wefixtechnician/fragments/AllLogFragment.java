package com.aahan.wefixtechnician.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.adapter.LogHistoryAdapter;
import com.aahan.wefixtechnician.model.LogResponse;
import com.aahan.wefixtechnician.model.Logs;
import com.aahan.wefixtechnician.storage.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<Logs> logsList;
    private TextView noRecord;
    private LogHistoryAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private EditText startDate, endDate;
    private Button search, searchAll;
    private Calendar myCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_log, container, false);
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

        noRecord = view.findViewById(R.id.no_record);
        noRecord.setVisibility(View.GONE);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        search = view.findViewById(R.id.search);
        searchAll = view.findViewById(R.id.searchAll);

        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        DatePickerDialog.OnDateSetListener date1 = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        };

        startDate.setOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        endDate.setOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(getActivity()), date1, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        search.setOnClickListener(
                v -> {
                    if (TextUtils.isEmpty(startDate.getText().toString()) || TextUtils.isEmpty(endDate.getText().toString())) {
                        Toast.makeText(getActivity(), "Enter the start and end Date to search", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            getLog(startDate.getText().toString(), endDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(getActivity(), startDate.getText().toString() + "\n" + endDate.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        searchAll.setOnClickListener(
                v -> getLog()
        );

    }

    public void getLog() {

        String txtStartDate = "2000-10-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String txtEndDate = sdf.format(new Date());

        progressBar.setVisibility(View.VISIBLE);
        int ref_technician_id = SharedPrefManager.getInstance(getActivity()).getTechnician().getTbl_technician_id();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCloseCallLogForTechnician(ref_technician_id, txtStartDate, txtEndDate);

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            if (logsList != null) {
                                logsList.clear();
                            }
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().equals("CANCEL") || logs1.getCallLogStatus().equals("CLOSE") || logs1.getCallLogStatus().equals("REJECT") || logs1.getCallLogStatus().equals("COMPLETE")) {
//                                    long date3 = 0;
//                                    try {
//                                        date3 = getMilliFromDate(logs1.getCallLogDate());
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (date3 >= date1 && date3 <= date2)
                                    logs.add(logs1);
                                }
                            }
                            recyclerView.setItemViewCacheSize(logs.size());
                            adapter = new LogHistoryAdapter(getActivity(), logs);
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            if (logs.isEmpty()) {
                                noRecord.setVisibility(View.VISIBLE);
                            } else {
                                noRecord.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        );

    }

    public void getLog(String txtStartDate, String txtEndDate) throws ParseException {

        progressBar.setVisibility(View.VISIBLE);
//        long date1 = getMilliFromDate(txtStartDate);
//        long date2 = getMilliFromDate(txtEndDate);

        int ref_technician_id = SharedPrefManager.getInstance(getActivity()).getTechnician().getTbl_technician_id();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCloseCallLogForTechnician(ref_technician_id, txtStartDate, txtEndDate);

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            if (logsList != null) {
                                logsList.clear();
                            }
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().equals("CANCEL") || logs1.getCallLogStatus().equals("CLOSE") || logs1.getCallLogStatus().equals("REJECT") || logs1.getCallLogStatus().equals("COMPLETE")) {
//                                    long date3 = 0;
//                                    try {
//                                        date3 = getMilliFromDate(logs1.getCallLogDate());
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (date3 >= date1 && date3 <= date2)
                                    logs.add(logs1);
                                }
                            }
                            recyclerView.setItemViewCacheSize(logs.size());
                            adapter = new LogHistoryAdapter(getActivity(), logs);
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            if (logs.isEmpty()) {
                                noRecord.setVisibility(View.VISIBLE);
                            } else {
                                noRecord.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        if (logsList != null) {
            logsList.clear();
        }
//        getLog();
//        if (logsList != null) {
        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
//        }
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

//    public long getMilliFromDate(String dateFormat) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = formatter.parse(dateFormat);
//        assert date != null;
//        return date.getTime();
//    }

}