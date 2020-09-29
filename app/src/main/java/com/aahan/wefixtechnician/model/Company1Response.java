package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Company1Response implements Serializable {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("company")
    @Expose
    Company company;

    public Company1Response(boolean error, Company company) {
        this.error = error;
        this.company = company;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
