package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartsResponse {

    @SerializedName("error")
    @Expose
    Boolean error;

    @SerializedName("parts")
    @Expose
    List<Parts> parts;

    public PartsResponse(Boolean error, List<Parts> parts) {
        this.error = error;
        this.parts = parts;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Parts> getParts() {
        return parts;
    }

    public void setParts(List<Parts> parts) {
        this.parts = parts;
    }
}
