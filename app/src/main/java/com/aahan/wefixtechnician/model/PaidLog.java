package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaidLog {

    @SerializedName("tbl_payment_id")
    @Expose
    int tblPaymentID;

    @SerializedName("category")
    @Expose
    String category;

    @SerializedName("ref_id")
    @Expose
    int refID;

    @SerializedName("no_of_call")
    @Expose
    int noOfCall;

    @SerializedName("total_amount")
    @Expose
    int totalAmount;

    @SerializedName("payment_date")
    @Expose
    String paymentDate;

    @SerializedName("entry_date")
    @Expose
    String entryDate;

    @SerializedName("entry_by")
    @Expose
    String entryBy;

    @SerializedName("uname")
    @Expose
    String uname;

    @SerializedName("entry_timestamp")
    @Expose
    String entryTimeStamp;

    @SerializedName("entry_ip")
    @Expose
    String entryIP;

    @SerializedName("naration")
    @Expose
    String naration;

    public PaidLog(int tblPaymentID, String category, int refID, int noOfCall, int totalAmount, String paymentDate, String entryDate, String entryBy, String uname, String entryTimeStamp, String entryIP, String naration) {
        this.tblPaymentID = tblPaymentID;
        this.category = category;
        this.refID = refID;
        this.noOfCall = noOfCall;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.entryDate = entryDate;
        this.entryBy = entryBy;
        this.uname = uname;
        this.entryTimeStamp = entryTimeStamp;
        this.entryIP = entryIP;
        this.naration = naration;
    }

    public int getTblPaymentID() {
        return tblPaymentID;
    }

    public void setTblPaymentID(int tblPaymentID) {
        this.tblPaymentID = tblPaymentID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRefID() {
        return refID;
    }

    public void setRefID(int refID) {
        this.refID = refID;
    }

    public int getNoOfCall() {
        return noOfCall;
    }

    public void setNoOfCall(int noOfCall) {
        this.noOfCall = noOfCall;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(String entryBy) {
        this.entryBy = entryBy;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEntryTimeStamp() {
        return entryTimeStamp;
    }

    public void setEntryTimeStamp(String entryTimeStamp) {
        this.entryTimeStamp = entryTimeStamp;
    }

    public String getEntryIP() {
        return entryIP;
    }

    public void setEntryIP(String entryIP) {
        this.entryIP = entryIP;
    }

    public String getNaration() {
        return naration;
    }

    public void setNaration(String naration) {
        this.naration = naration;
    }
}
