package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Service implements Serializable {

    @SerializedName("tbl_services_id")
    @Expose
    private int tbl_services_id;

    @SerializedName("cat_ref_id")
    @Expose
    private int cat_ref_id;

    @SerializedName("tbl_services_name")
    @Expose
    private String tbl_services_name;

    @SerializedName("tbl_services_des")
    @Expose
    private String tbl_services_des;

    @SerializedName("tbl_services_charge")
    @Expose
    private float tbl_services_charge;

    @SerializedName("tbl_delear_services_charge")
    @Expose
    private double tblDelearServicesCharge;

    @SerializedName("status")
    @Expose
    private String status;

    public Service(int tbl_services_id, int cat_ref_id, String tbl_services_name, String tbl_services_des, float tbl_services_charge, double tblDelearServicesCharge, String status) {
        this.tbl_services_id = tbl_services_id;
        this.cat_ref_id = cat_ref_id;
        this.tbl_services_name = tbl_services_name;
        this.tbl_services_des = tbl_services_des;
        this.tbl_services_charge = tbl_services_charge;
        this.tblDelearServicesCharge = tblDelearServicesCharge;
        this.status = status;
    }

    public int getTbl_services_id() {
        return tbl_services_id;
    }

    public void setTbl_services_id(int tbl_services_id) {
        this.tbl_services_id = tbl_services_id;
    }

    public int getCat_ref_id() {
        return cat_ref_id;
    }

    public void setCat_ref_id(int cat_ref_id) {
        this.cat_ref_id = cat_ref_id;
    }

    public String getTbl_services_name() {
        return tbl_services_name;
    }

    public void setTbl_services_name(String tbl_services_name) {
        this.tbl_services_name = tbl_services_name;
    }

    public String getTbl_services_des() {
        return tbl_services_des;
    }

    public void setTbl_services_des(String tbl_services_des) {
        this.tbl_services_des = tbl_services_des;
    }

    public float getTbl_services_charge() {
        return tbl_services_charge;
    }

    public void setTbl_services_charge(float tbl_services_charge) {
        this.tbl_services_charge = tbl_services_charge;
    }

    public double getTblDelearServicesCharge() {
        return tblDelearServicesCharge;
    }

    public void setTblDelearServicesCharge(double tblDelearServicesCharge) {
        this.tblDelearServicesCharge = tblDelearServicesCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
