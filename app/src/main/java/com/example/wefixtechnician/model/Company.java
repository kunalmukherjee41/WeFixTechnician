package com.example.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Company implements Serializable {

    @SerializedName("tbl_company_id")
    @Expose
    int tblCompanyId;

    @SerializedName("tbl_company_name")
    @Expose
    String tblCompanyName;

    @SerializedName("status")
    @Expose
    String status;

    public Company(int tblCompanyId, String tblCompanyName, String status) {
        this.tblCompanyId = tblCompanyId;
        this.tblCompanyName = tblCompanyName;
        this.status = status;
    }

    public int getTblCompanyId() {
        return tblCompanyId;
    }

    public void setTblCompanyId(int tblCompanyId) {
        this.tblCompanyId = tblCompanyId;
    }

    public String getTblCompanyName() {
        return tblCompanyName;
    }

    public void setTblCompanyName(String tblCompanyName) {
        this.tblCompanyName = tblCompanyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

