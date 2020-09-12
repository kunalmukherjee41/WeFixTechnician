package com.example.wefixtechnician.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wefixtechnician.R;
import com.example.wefixtechnician.model.WarrantyLog;

import java.util.Objects;

public class WarrantyLogHistoryActivity extends AppCompatActivity {

    WarrantyLog warrantyLog;

    TextView date, name, address;
    TextView pin, email, phone;
    TextView model, serial, purchaseDate;
    TextView problem, status, company;
    TextView category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty_log_history);

        warrantyLog = (WarrantyLog) Objects.requireNonNull(getIntent().getExtras()).getSerializable("log");
        String txt_company = getIntent().getExtras().getString("company");

        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        pin = findViewById(R.id.pin);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        model = findViewById(R.id.model);
        serial = findViewById(R.id.serial);
        purchaseDate = findViewById(R.id.purchase_date);
        status = findViewById(R.id.status);
        problem = findViewById(R.id.problem);
        company = findViewById(R.id.company);
        category = findViewById(R.id.category);

        date.setText(warrantyLog.getCallLogDate());
        name.setText(warrantyLog.getClientName());
        address.setText(warrantyLog.getClientAddress());
        pin.setText(warrantyLog.getClientPin());
        email.setText(warrantyLog.getClientEmail());
        phone.setText(warrantyLog.getClientMb());
        model.setText(warrantyLog.getModelNo());
        serial.setText(warrantyLog.getSerialNo());
        purchaseDate.setText(warrantyLog.getPurchaseDate());
        status.setText(warrantyLog.getCallLogStatus());
        problem.setText(warrantyLog.getProblem());
        company.setText(txt_company);
        category.setText(warrantyLog.getCategory());

    }
}