package com.example.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WarrantyLog implements Serializable {

    @SerializedName("call_log_id")
    @Expose
    public int callLogId;

    @SerializedName("call_log_date")
    @Expose
    public String callLogDate;

    @SerializedName("call_log_type")
    @Expose
    public String callLogType;

    @SerializedName("client_name")
    @Expose
    public String clientName;

    @SerializedName("client_address")
    @Expose
    public String clientAddress;

    @SerializedName("client_pin")
    @Expose
    public String clientPin;

    @SerializedName("client_mb")
    @Expose
    public String clientMb;

    @SerializedName("client_email")
    @Expose
    public String clientEmail;

    @SerializedName("ref_company_id")
    @Expose
    public int refCompanyId; ///////////

    @SerializedName("model_no")
    @Expose
    public String modelNo;

    @SerializedName("serial_no")
    @Expose
    public String serialNo;

    @SerializedName("purchase_date")
    @Expose
    public String purchaseDate;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("amount")
    @Expose
    public int amount;

    @SerializedName("payment_type")
    @Expose
    public String paymentType;

    @SerializedName("problem")
    @Expose
    public String problem;

    @SerializedName("call_log_status")
    @Expose
    public String callLogStatus;

    @SerializedName("call_log_transfer_id")
    @Expose
    public int callLogTransferId;

    @SerializedName("ref_technician_id")
    @Expose
    public int refTechnicianId;

    @SerializedName("rejected_reason")
    @Expose
    public int rejectedReason;

    @SerializedName("category")
    @Expose
    private String category;

    public WarrantyLog(int callLogId, String callLogDate, String callLogType, String clientName, String clientAddress, String clientPin, String clientMb, String clientEmail, int refCompanyId, String modelNo, String serialNo, String purchaseDate, String image, int amount, String paymentType, String problem, String callLogStatus, int callLogTransferId, int refTechnicianId, int rejectedReason, String category) {
        this.callLogId = callLogId;
        this.callLogDate = callLogDate;
        this.callLogType = callLogType;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientPin = clientPin;
        this.clientMb = clientMb;
        this.clientEmail = clientEmail;
        this.refCompanyId = refCompanyId;
        this.modelNo = modelNo;
        this.serialNo = serialNo;
        this.purchaseDate = purchaseDate;
        this.image = image;
        this.amount = amount;
        this.paymentType = paymentType;
        this.problem = problem;
        this.callLogStatus = callLogStatus;
        this.callLogTransferId = callLogTransferId;
        this.refTechnicianId = refTechnicianId;
        this.rejectedReason = rejectedReason;
        this.category = category;
    }

    public int getCallLogId() {
        return callLogId;
    }

    public void setCallLogId(int callLogId) {
        this.callLogId = callLogId;
    }

    public String getCallLogDate() {
        return callLogDate;
    }

    public void setCallLogDate(String callLogDate) {
        this.callLogDate = callLogDate;
    }

    public String getCallLogType() {
        return callLogType;
    }

    public void setCallLogType(String callLogType) {
        this.callLogType = callLogType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientPin() {
        return clientPin;
    }

    public void setClientPin(String clientPin) {
        this.clientPin = clientPin;
    }

    public String getClientMb() {
        return clientMb;
    }

    public void setClientMb(String clientMb) {
        this.clientMb = clientMb;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getRefCompanyId() {
        return refCompanyId;
    }

    public void setRefCompanyId(int refCompanyId) {
        this.refCompanyId = refCompanyId;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getCallLogStatus() {
        return callLogStatus;
    }

    public void setCallLogStatus(String callLogStatus) {
        this.callLogStatus = callLogStatus;
    }

    public int getCallLogTransferId() {
        return callLogTransferId;
    }

    public void setCallLogTransferId(int callLogTransferId) {
        this.callLogTransferId = callLogTransferId;
    }

    public int getRefTechnicianId() {
        return refTechnicianId;
    }

    public void setRefTechnicianId(int refTechnicianId) {
        this.refTechnicianId = refTechnicianId;
    }

    public int getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(int rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
