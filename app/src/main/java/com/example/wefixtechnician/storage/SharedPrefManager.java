package com.example.wefixtechnician.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wefixtechnician.model.Technician;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveFirebaseId(int firebase) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("ID", firebase);
    }

    public boolean isLoggedFirebase() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("ID", -1) != -1;
    }

    public void saveTechnician(Technician technician) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("tbl_technician_id", technician.getTbl_technician_id());
        editor.putString("technician_name", technician.getTechnician_name());
        editor.putString("service_type", technician.getService_type());
        editor.putString("service_des", technician.getService_des());
        editor.putString("address", technician.getAddress());
        editor.putString("pin", technician.getPin());
        editor.putString("contact1", technician.getContact1());
        editor.putString("contact2", technician.getContact2());
        editor.putString("panno", technician.getPanno());
        editor.putString("gstin", technician.getGstin());
        editor.putString("email", technician.getEmail());
        editor.putString("website", technician.getWebsite());
        editor.putString("status", technician.getStatus());
        editor.putString("username", technician.getUsernmae());
        editor.putString("password", technician.getPassword());
        editor.putInt("ref_servicecenter", technician.getRef_servicecenter());

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("tbl_technician_id", -1) != -1;
    }

    public Technician getTechnician() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new Technician(

                sharedPreferences.getInt("tbl_technician_id", -1),
                sharedPreferences.getString("technician_name", null),
                sharedPreferences.getString("service_type", null),
                sharedPreferences.getString("service_des", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("pin", null),
                sharedPreferences.getString("contact1", null),
                sharedPreferences.getString("contact2", null),
                sharedPreferences.getString("panno", null),
                sharedPreferences.getString("gstin", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("website", null),
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getInt("ref_servicecenter", -1)

        );
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

    }

}
