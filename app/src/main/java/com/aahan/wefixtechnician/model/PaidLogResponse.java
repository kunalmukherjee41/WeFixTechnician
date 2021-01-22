package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaidLogResponse {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("paidlog")
    @Expose
    List<PaidLog> paidLogs;

    public PaidLogResponse(boolean error, List<PaidLog> paidLogs) {
        this.error = error;
        this.paidLogs = paidLogs;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<PaidLog> getPaidLogs() {
        return paidLogs;
    }

    public void setPaidLogs(List<PaidLog> paidLogs) {
        this.paidLogs = paidLogs;
    }

}
