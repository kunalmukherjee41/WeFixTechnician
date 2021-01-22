package com.aahan.wefixtechnician.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.BuildConfig;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.model.Category;
import com.aahan.wefixtechnician.model.Logs;
import com.aahan.wefixtechnician.model.Parts;
import com.aahan.wefixtechnician.model.PartsResponse;
import com.aahan.wefixtechnician.model.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private long currentTime;
    private String serviceName;
    private String rate_1, txt_cgst1;

    private TextView partsAmount;
    private TextView categoryTextView;
    private TextView total;
    private Logs logs;

    private List<Parts> parts;
    int pageWidth = 850;
    int pageHeight = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_log);

        txtPartsDesc = new StringBuffer();

        Intent intent = getIntent();
        serviceName = intent.getStringExtra("name");

        assert serviceName != null;
        if (serviceName.equals("visit")) {
            service_id = intent.getIntExtra("service", 0);
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
        if (parts != null)
            progressBar.setVisibility(View.GONE);

        Call<PartsResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getParts(logs.getCallLogId());

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
                                if (logs.getRefServiceId() == service.getTbl_services_id()) {
                                    totalAmount = txtPartAmount + logs.getAmount();
                                } else {
                                    if (logs.getRefDelear() != 0) {
                                        totalAmount = (float) (txtPartAmount + service.getTblDelearServicesCharge());
                                    } else {
                                        totalAmount = txtPartAmount + service.getTbl_services_charge();
                                    }
                                }
                            } else {
                                totalAmount = txtPartAmount + 268;
                            }
                            total.setText(String.valueOf(totalAmount));
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

        TextView invoice = findViewById(R.id.invoice);
        TextView date = findViewById(R.id.date);
        TextView serviceTextView = findViewById(R.id.service);
        categoryTextView = findViewById(R.id.category);
        TextView quantity = findViewById(R.id.quantity);
        TextView rate = findViewById(R.id.rate);
        TextView cgst = findViewById(R.id.cgst);
        TextView sgst = findViewById(R.id.sgst);
        total = findViewById(R.id.total);

        TextView name = findViewById(R.id.customer_name);
        TextView address = findViewById(R.id.customer_address);
        TextView phone = findViewById(R.id.customer_phone);
        TextView email = findViewById(R.id.customer_email);
        TextView callID = findViewById(R.id.call);

        callID.setText(String.valueOf(logs.getCallLogId()));

        Button complete = findViewById(R.id.complete);

        Button showPDF = findViewById(R.id.show_pdf);
        Button downloadPDF = findViewById(R.id.download_pdf);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = sdf.format(new Date());

//        Date currentTime = Calendar.getInstance().getTime();
        currentTime = System.currentTimeMillis();

        invoiceNum = logs.getCallLogId() + "1";
        invoice.setText(invoiceNum);
        date.setText(currentDate);

        serviceTextView.setText(serviceName);

        quantity.setText("1");
        double rate_;
        double txt_cgst;
        if (serviceName.equals("visit")) {
            rate_ = 268 / 1.18;
//            txt_cgst = 268 * 0.09;
        } else {
            if (logs.getRefServiceId() == service.getTbl_services_id()) {
                rate_ = logs.getAmount() / 1.18;
//                txt_cgst = logs.getAmount() * 0.09;
            } else {
                if (logs.getRefDelear() != 0) {
                    rate_ = service.getTblDelearServicesCharge() / 1.18;
                } else {
                    rate_ = service.getTbl_services_charge() / 1.18;
                }
//                txt_cgst = service.getTblDelearServicesCharge() * 0.09;
            }
        }
        txt_cgst = rate_ * 0.09;
        rate_1 = String.format(Locale.CANADA, "%.2f", rate_);
        rate.setText(rate_1);
        txt_cgst1 = String.format(Locale.CANADA, "%.2f", txt_cgst);
        cgst.setText(txt_cgst1);
        sgst.setText(txt_cgst1);

        name.setText(logs.getClientName());
        address.setText(logs.getClientAddress());
        phone.setText(logs.getClientMb());
        email.setText(logs.getClientEmail());

        complete.setVisibility(View.VISIBLE);
        showPDF.setVisibility(View.GONE);
        downloadPDF.setVisibility(View.GONE);
        if (logs.getCallLogStatus().equals("COMPLETE")) {
            complete.setVisibility(View.GONE);
            showPDF.setVisibility(View.VISIBLE);
            downloadPDF.setVisibility(View.VISIBLE);
        }

        downloadPDF.setOnClickListener(
                v -> {
                    if (isStoragePermissionGranted(this))
                        sendPdfToCustomer("Aa");
                }
        );

        showPDF.setOnClickListener(
                v -> {
                    if (isStoragePermissionGranted(this))
                        sendPdfToCustomer("show");
                }
        );

        complete.setOnClickListener(
                v -> {
//                        sendPdfToCustomer()
                    if (isStoragePermissionGranted(this)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(CompleteLogActivity.this));
                        builder.setMessage("Are You Want To get A copy for Customer?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, id) -> {
                                    sendPdfToCustomer("aa");
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Objects.requireNonNull(CompleteLogActivity.this));
                                    builder1.setMessage("Are you want Complete the Call Log?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", (dialog1, id1) -> {

                                                int call_log_id = logs.getCallLogId();
                                                Call<ResponseBody> call1 = RetrofitClient
                                                        .getInstance()
                                                        .getApi()
                                                        .completeCallLog(call_log_id, service_id, "COMPLETE", amount, currentDate, String.valueOf(currentTime));

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
                                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent1);
                                                startActivity(intent1);
                                                finish();
                                            })
                                            .setNegativeButton("No", (dialog1, id1) -> dialog1.cancel());
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
                                                        .completeCallLog(call_log_id, service_id, "COMPLETE", amount, currentDate, String.valueOf(currentTime));

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
                                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent1);
                                                finish();
                                            })
                                            .setNegativeButton("No", (dialog1, id1) -> dialog1.cancel());
                                    AlertDialog alert = builder1.create();
                                    alert.show();
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
        );
    }

    private void sendPdfToCustomer(String message) {
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
        canvas.drawLine(pageWidth / 2 - 30, 490, pageWidth / 2 - 30, 950, paint);
        canvas.drawLine(470, 490, 470, 950, paint);
        canvas.drawLine(560, 490, 560, 950, paint);

        canvas.drawLine(625, 910, 625, 950, paint);
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
        canvas.drawText("9%", 400, 530, paint);
        canvas.drawText("SGST", 480, 510, paint);
        canvas.drawText("9%", 480, 530, paint);
        canvas.drawText("Amount", 580, 510, paint);

        canvas.drawText("TOTAL-", 560, 935, paint);

        if (category.getTbl_category_name() != null)
            canvas.drawText(category.getTbl_category_name(), 40, 570, paint);
        canvas.drawText("9954", 240, 570, paint);
        canvas.drawText(rate_1, 310, 570, paint);
        canvas.drawText(txt_cgst1, 400, 570, paint);
        canvas.drawText(txt_cgst1, 480, 570, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        if (serviceName.equals("visit"))
            canvas.drawText("268.00", pageWidth - 30, 570, paint);
        else
            canvas.drawText(String.valueOf(service.getTbl_services_charge()), pageWidth - 30, 570, paint);
        int i = 580;
        for (Parts p : parts) {
            paint.setTextAlign(Paint.Align.LEFT);
            double pRate_ = p.getAmount() / 1.18;
            canvas.drawText(p.getPartsDes(), 40, i + 40, paint);
            canvas.drawText(String.format(Locale.CANADA, "%.2f", pRate_), 310, i + 40, paint);
            canvas.drawText(String.format(Locale.CANADA, "%.2f", pRate_ * 0.09), 400, i + 40, paint);
            canvas.drawText(String.format(Locale.CANADA, "%.2f", pRate_ * 0.09), 480, i + 40, paint);
            canvas.drawText("8504", 240, i + 40, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(p.getAmount()), pageWidth - 30, i + 40, paint);

            i += 40;
        }

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(18f);
        canvas.drawText(String.valueOf(totalAmount), pageWidth - 30, 935, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(12f);
        canvas.drawText("No Warranty Cover on any parts and any Services", 30, 990, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(18f);
        canvas.drawText("Company's PAN: ABMFA5193K", pageWidth - 60, 1030, paint);
        canvas.drawText("AAHAN COMMERCIAL AND CO.", pageWidth - 60, 1060, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(14f);
        canvas.drawText("This Is System Generated Bill", pageWidth / 2, pageHeight - 30, paint);

        invoicePdf.finishPage(page);

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = "/invoice " + logs.getCallLogId() + ".pdf";
        File file = new File(Environment.getExternalStorageDirectory(), path);

        FileOutputStream outputStream;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, true);

            invoicePdf.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
            if (message.equals("show")) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uriPDF = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uriPDF, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            invoicePdf.writeTo(new FileOutputStream(file));
//            if (message.equals("show")) {
//                progressBar.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri swatchImageUri = FileProvider.getUriForFile(this,
//                        BuildConfig.APPLICATION_ID + ".provider", file);
//                intent.setDataAndType(swatchImageUri, "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(intent);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
