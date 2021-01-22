package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SumAllLogResponse {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("total")
    @Expose
    int total;

    public SumAllLogResponse(boolean error, int total) {
        this.error = error;
        this.total = total;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
