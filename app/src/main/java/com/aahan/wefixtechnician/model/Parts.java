package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parts {

    @SerializedName("log_parts_id")
    @Expose
    int logPartsID;

    @SerializedName("ref_log_id")
    @Expose
    int refLogID;

    @SerializedName("ref_technician_id")
    @Expose
    int refTechnicianID;

    @SerializedName("partdes")
    @Expose
    String partsDes;

    @SerializedName("amount")
    @Expose
    double amount;

    @SerializedName("entry_date")
    @Expose
    String entryDate;

    @SerializedName("entry_time")
    @Expose
    String entryTime;

    public Parts(int logPartsID, int refLogID, int refTechnicianID, String partsDes, double amount, String entryDate, String entryTime) {
        this.logPartsID = logPartsID;
        this.refLogID = refLogID;
        this.refTechnicianID = refTechnicianID;
        this.partsDes = partsDes;
        this.amount = amount;
        this.entryDate = entryDate;
        this.entryTime = entryTime;
    }

    public int getLogPartsID() {
        return logPartsID;
    }

    public void setLogPartsID(int logPartsID) {
        this.logPartsID = logPartsID;
    }

    public int getRefLogID() {
        return refLogID;
    }

    public void setRefLogID(int refLogID) {
        this.refLogID = refLogID;
    }

    public int getRefTechnicianID() {
        return refTechnicianID;
    }

    public void setRefTechnicianID(int refTechnicianID) {
        this.refTechnicianID = refTechnicianID;
    }

    public String getPartsDes() {
        return partsDes;
    }

    public void setPartsDes(String partsDes) {
        this.partsDes = partsDes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }
}
