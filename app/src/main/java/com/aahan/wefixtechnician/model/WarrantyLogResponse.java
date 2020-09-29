package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WarrantyLogResponse implements Serializable {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("log")
    @Expose
    List<WarrantyLog> warrantyLogList;

    public WarrantyLogResponse(boolean error, List<WarrantyLog> warrantyLogList) {
        this.error = error;
        this.warrantyLogList = warrantyLogList;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<WarrantyLog> getWarrantyLogList() {
        return warrantyLogList;
    }

    public void setWarrantyLogList(List<WarrantyLog> warrantyLogList) {
        this.warrantyLogList = warrantyLogList;
    }
}
