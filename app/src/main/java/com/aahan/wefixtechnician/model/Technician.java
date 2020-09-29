package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Technician implements Serializable {

    @SerializedName("tbl_technician_id")
    @Expose
    int tbl_technician_id;
    @SerializedName("technician_name")
    @Expose
    String technician_name;
    @SerializedName("service_type")
    @Expose
    String service_type;
    @SerializedName("service_des")
    @Expose
    String service_des;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("pin")
    @Expose
    String pin;
    @SerializedName("contact1")
    @Expose
    String contact1;
    @SerializedName("contact2")
    @Expose
    String contact2;
    @SerializedName("panno")
    @Expose
    String panno;
    @SerializedName("gstin")
    @Expose
    String gstin;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("website")
    @Expose
    String website;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("usernmae")
    @Expose
    String usernmae;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("ref_servicecenter")
    @Expose
    int ref_servicecenter;

    public Technician(int tbl_technician_id, String technician_name, String service_type, String service_des, String address, String pin, String contact1, String contact2, String panno, String gstin, String email, String website, String status, String usernmae, String password, int ref_servicecenter) {
        this.tbl_technician_id = tbl_technician_id;
        this.technician_name = technician_name;
        this.service_type = service_type;
        this.service_des = service_des;
        this.address = address;
        this.pin = pin;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.panno = panno;
        this.gstin = gstin;
        this.email = email;
        this.website = website;
        this.status = status;
        this.usernmae = usernmae;
        this.password = password;
        this.ref_servicecenter = ref_servicecenter;
    }

    public int getTbl_technician_id() {
        return tbl_technician_id;
    }

    public void setTbl_technician_id(int tbl_technician_id) {
        this.tbl_technician_id = tbl_technician_id;
    }

    public String getTechnician_name() {
        return technician_name;
    }

    public void setTechnician_name(String technician_name) {
        this.technician_name = technician_name;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getService_des() {
        return service_des;
    }

    public void setService_des(String service_des) {
        this.service_des = service_des;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getPanno() {
        return panno;
    }

    public void setPanno(String panno) {
        this.panno = panno;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsernmae() {
        return usernmae;
    }

    public void setUsernmae(String usernmae) {
        this.usernmae = usernmae;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRef_servicecenter() {
        return ref_servicecenter;
    }

    public void setRef_servicecenter(int ref_servicecenter) {
        this.ref_servicecenter = ref_servicecenter;
    }
}
