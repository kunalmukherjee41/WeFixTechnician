package com.example.wefixtechnician.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.model.Category;
import com.example.wefixtechnician.model.Logs;
import com.example.wefixtechnician.model.Parts;
import com.example.wefixtechnician.model.PartsResponse;
import com.example.wefixtechnician.model.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.SimpleTimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteLogActivity extends AppCompatActivity {

    private Category category;
    private Service service;

    private float txtPartAmount = 0, totalAmount;

    private ProgressBar progressBar;

    private int service_id;
    private String amount, invoiceNum;
    private StringBuffer txtPartsDesc;

    private String currentDate;
    private Date currentTime;
    private String serviceName;
    private String rate_1, txt_cgst1;

    private TextView partsAmount;
    private TextView invoice, date, serviceTextView;
    private TextView categoryTextView, quantity;
    private TextView rate, cgst, sgst, total, callID;
    private Logs logs;
    private TextView name, address, phone, email;
    private Button complete;

    private List<Parts> parts;
    private int pageWidth = 750;
    private int pageHeight = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_log);

        txtPartsDesc = new StringBuffer();

        Intent intent = getIntent();
        serviceName = intent.getStringExtra("name");

        assert serviceName != null;
        if (serviceName.equals("visit")) {
            int serviceID = intent.getIntExtra("service", 0);
            service_id = serviceID;
            amount = "268";
        } else {
            service = (Service) intent.getSerializableExtra("service");
            assert service != null;
            service_id = service.getTbl_services_id();
            amount = String.valueOf(service.getTbl_services_charge());
        }
        category = (Category) intent.getSerializableExtra("category");
        logs = (Logs) intent.getSerializableExtra("log");

//        categoryTextView.setText(category.getTbl_category_name());

        partsAmount = findViewById(R.id.parts_amount);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Call<PartsResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getParts(logs.getCallLogId(), "app");

        call.enqueue(
                new Callback<PartsResponse>() {
                    @Override
                    public void onResponse(Call<PartsResponse> call, Response<PartsResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            progressBar.setVisibility(View.GONE);
//                            if (null != response.body().getParts().getPartsDes()) {
                            parts = response.body().getParts();
                            txtPartsDesc.append(category.getTbl_category_name());
                            if (!parts.isEmpty()) {
                                for (Parts p : parts) {
                                    txtPartsDesc.append("\n").append(p.getPartsDes());
                                    txtPartAmount += p.getAmount();
                                }
                            }
//                            Toast.makeText(CompleteLogActivity.this, txtPartsDesc, Toast.LENGTH_SHORT).show();
                            categoryTextView.setText(txtPartsDesc);
                            partsAmount.setText(String.valueOf(txtPartAmount));
                            if (!serviceName.equals("visit")) {
                                totalAmount = txtPartAmount + service.getTbl_services_charge();
                                total.setText(String.valueOf(totalAmount));
                            } else {
                                totalAmount = txtPartAmount + 268;
                                total.setText(String.valueOf(totalAmount));
                            }
//                            if (parts.get(0).getPartsDes() != null) {
//                                String text = category.getTbl_category_name() + "\n" + parts.get(0).getPartsDes();
////                                Toast.makeText(CompleteLogActivity.this, text, Toast.LENGTH_SHORT).show();
//                                categoryTextView.setText(text);
//                            } else {
////                                }
//                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PartsResponse> call, Throwable t) {
                        Toast.makeText(CompleteLogActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        invoice = findViewById(R.id.invoice);
        date = findViewById(R.id.date);
        serviceTextView = findViewById(R.id.service);
        categoryTextView = findViewById(R.id.category);
        quantity = findViewById(R.id.quantity);
        rate = findViewById(R.id.rate);
        cgst = findViewById(R.id.cgst);
        sgst = findViewById(R.id.sgst);
        total = findViewById(R.id.total);

        name = findViewById(R.id.customer_name);
        address = findViewById(R.id.customer_address);
        phone = findViewById(R.id.customer_phone);
        email = findViewById(R.id.customer_email);
        callID = findViewById(R.id.call);

        callID.setText(String.valueOf(logs.getCallLogId()));

        complete = findViewById(R.id.complete);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        currentDate = sdf.format(new Date());
        currentTime = Calendar.getInstance().getTime();

        invoiceNum = logs.getCallLogId() + "1";
        invoice.setText(invoiceNum);
        date.setText(currentDate);

        serviceTextView.setText(serviceName);

        quantity.setText("1");
        double rate_;
        if (serviceName.equals("visit")) {
            rate_ = 268 / 1.18;
        } else {
            rate_ = service.getTbl_services_charge() / 1.18;
        }
        rate_1 = String.format(Locale.CANADA, "%.2f", rate_);
        rate.setText(rate_1);
        double txt_cgst = rate_ - rate_ * 0.9;
        txt_cgst1 = String.format(Locale.CANADA, "%.2f", txt_cgst);
        cgst.setText(txt_cgst1);
        sgst.setText(txt_cgst1);

        name.setText(logs.getClientName());
        address.setText(logs.getClientAddress());
        phone.setText(logs.getClientMb());
        email.setText(logs.getClientEmail());

        complete.setOnClickListener(
                v -> {

                    if (isStoragePermissionGranted(this)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(CompleteLogActivity.this));
                        builder.setMessage("Are You Want To Send A copy to Customer?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, id) -> {
                                    sendPdfToCustomer();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Objects.requireNonNull(CompleteLogActivity.this));
                                    builder1.setMessage("Are you want Complete the Call Log?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", (dialog1, id1) -> {

                                                int call_log_id = logs.getCallLogId();
                                                Call<ResponseBody> call1 = RetrofitClient
                                                        .getInstance()
                                                        .getApi()
                                                        .completeCallLog(call_log_id, service_id, "COMPLETE", amount);

                                                call1.enqueue(
                                                        new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                            }
                                                        }
                                                );

                                                Intent intent1 = new Intent(CompleteLogActivity.this, MessageActivity.class);
                                                startActivity(intent1);
                                            })
                                            .setNegativeButton("No", (dialog1, id1) -> {
                                                dialog1.cancel();
                                            });
                                    AlertDialog alert = builder1.create();
                                    alert.show();
                                })
                                .setNegativeButton("No", (dialog, id) -> {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Objects.requireNonNull(CompleteLogActivity.this));
                                    builder1.setMessage("Are you want Complete the Call Log?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", (dialog1, id1) -> {

                                                int call_log_id = logs.getCallLogId();
                                                Call<ResponseBody> call1 = RetrofitClient
                                                        .getInstance()
                                                        .getApi()
                                                        .completeCallLog(call_log_id, service_id, "COMPLETE", amount);

                                                call1.enqueue(
                                                        new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                            }
                                                        }
                                                );

                                                Intent intent1 = new Intent(CompleteLogActivity.this, MessageActivity.class);
                                                startActivity(intent1);
                                            })
                                            .setNegativeButton("No", (dialog1, id1) -> {
                                                dialog1.cancel();
                                            });
                                    AlertDialog alert = builder1.create();
                                    alert.show();
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
        );
    }

    private void sendPdfToCustomer() {
        PdfDocument invoicePdf = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = invoicePdf.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20, 40, pageWidth - 20, 950, paint);

        canvas.drawLine(20, 540, pageWidth - 20, 540, paint);

        canvas.drawLine(220, 490, 220, 950, paint);
        canvas.drawLine(300, 490, 300, 950, paint);
        canvas.drawLine(pageWidth / 2 + 5, 490, pageWidth / 2, 950, paint);
        canvas.drawLine(460, 490, 460, 950, paint);
        canvas.drawLine(530, 490, 530, 950, paint);

        canvas.drawLine(610, 910, 610, 950, paint);
        canvas.drawLine(20, 910, pageWidth - 20, 910, paint);

        canvas.drawLine(20, 320, pageWidth - 20, 320, paint);

        canvas.drawLine(pageWidth / 2, 320, pageWidth / 2, 490, paint);
        canvas.drawLine(20, 490, pageWidth - 20, 490, paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70);
        canvas.drawText("WeFix", pageWidth / 2, 120, titlePaint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(18f);
        paint.setColor(Color.rgb(60, 60, 60));
        canvas.drawText("AAHAN COMMERCIAL AND CO", pageWidth / 2, 150, paint);
        canvas.drawText("GSTIN-19ABMFA5193K1ZU", pageWidth / 2, 180, paint);
        canvas.drawText("Contact-8509200714", pageWidth / 2, 210, paint);
        canvas.drawText("E-mail- wefix.aahan@gmail.com", pageWidth / 2, 240, paint);
        canvas.drawText("Web Site- www.wefixservice.in", pageWidth / 2, 270, paint);
        canvas.drawText("WEST BENGAL-19", pageWidth / 2, 300, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(18f);
        canvas.drawText("Customer Name: ", 30, 340, paint);
        canvas.drawText("Address: ", 30, 380, paint);
        canvas.drawText("Contact: ", 30, 420, paint);
        canvas.drawText("E-mail: ", 30, 460, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(18f);
        canvas.drawText(logs.getClientName(), pageWidth / 2 - 20, 340, paint);
        canvas.drawText(logs.getClientAddress(), pageWidth / 2 - 20, 380, paint);
        canvas.drawText(logs.getClientMb(), pageWidth / 2 - 20, 420, paint);
        canvas.drawText(logs.getClientEmail(), pageWidth / 2 - 20, 460, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(18f);
        canvas.drawText("Invoice No.: ", pageWidth / 2 + 10, 340, paint);
        canvas.drawText("Call Log No.: ", pageWidth / 2 + 10, 380, paint);
        canvas.drawText("Date: ", pageWidth / 2 + 10, 420, paint);
        canvas.drawText("Work Type: ", pageWidth / 2 + 10, 460, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(18f);
        canvas.drawText(invoiceNum, pageWidth - 30, 340, paint);
        canvas.drawText(String.valueOf(logs.getCallLogId()), pageWidth - 30, 380, paint);
        canvas.drawText(currentDate, pageWidth - 30, 420, paint);
        canvas.drawText(serviceName, pageWidth - 30, 460, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(18f);
        canvas.drawText("Description of", 40, 510, paint);
        canvas.drawText("Service Parts", 40, 530, paint);
        canvas.drawText("HSN", 240, 510, paint);
        canvas.drawText("Rate", 320, 510, paint);
        canvas.drawText("CGST", 400, 510, paint);
        canvas.drawText("%9", 400, 530, paint);
        canvas.drawText("SGST", 480, 510, paint);
        canvas.drawText("%9", 480, 530, paint);
        canvas.drawText("Amount", 580, 510, paint);

        canvas.drawText("TOTAL-", 540, 935, paint);

        canvas.drawText(category.getTbl_category_name(), 40, 570, paint);
        canvas.drawText(rate_1, 320, 570, paint);
        canvas.drawText(txt_cgst1, 400, 570, paint);
        canvas.drawText(txt_cgst1, 480, 570, paint);
        if (serviceName.equals("visit"))
            canvas.drawText("268", 580, 570, paint);
        else
            canvas.drawText(String.valueOf(service.getTbl_services_charge()), 580, 570, paint);
        int i = 580;
        for (Parts p : parts) {
            canvas.drawText(p.getPartsDes(), 40, i + 40, paint);
            canvas.drawText(String.valueOf(p.getAmount()), 580, i + 40, paint);
        }

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(18f);
        canvas.drawText(String.valueOf(totalAmount), pageWidth - 30, 935, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(18f);
        canvas.drawText("Company's PAN: ABMFA5193K", pageWidth - 60, 1020, paint);
        canvas.drawText("AAHAN COMMERCIAL AND CO.", pageWidth - 60, 1050, paint);

        invoicePdf.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory(), "/invoice " + currentTime + ".pdf");

        try {
            invoicePdf.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        invoicePdf.close();
    }

    public boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(CompleteLogActivity.this, "External Permission Granted", Toast.LENGTH_SHORT).show();
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            Toast.makeText(CompleteLogActivity.this, "External Permission Required", Toast.LENGTH_SHORT).show();
    }

}
