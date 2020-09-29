package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LogResponse implements Serializable {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("log")
    @Expose
    private List<Logs> log;

    public LogResponse(Boolean error, List<Logs> log) {
        this.error = error;
        this.log = log;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Logs> getLog() {
        return log;
    }

    public void setLog(List<Logs> log) {
        this.log = log;
    }
}
