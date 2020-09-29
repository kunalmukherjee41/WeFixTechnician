package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Logs implements Serializable {

    @SerializedName("call_log_id")
    @Expose
    private int callLogId;

    @SerializedName("call_log_date")
    @Expose
    private String callLogDate;

    @SerializedName("client_ref_id")
    @Expose
    int clientRefID;

    @SerializedName("call_log_type")
    @Expose
    private String callLogType;

    @SerializedName("client_name")
    @Expose
    private String clientName;

    @SerializedName("client_address")
    @Expose
    private String clientAddress;

    @SerializedName("client_pin")
    @Expose
    private String clientPin;

    @SerializedName("client_mb")
    @Expose
    private String clientMb;

    @SerializedName("client_email")
    @Expose
    private String clientEmail;

    @SerializedName("ref_cat_id")
    @Expose
    private int refCatId;

    @SerializedName("ref_service_id")
    @Expose
    private int refServiceId;

    @SerializedName("call_company_id")
    @Expose
    private int callCompanyId;

    @SerializedName("product_company")
    @Expose
    private String productCompany;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("payment_type")
    @Expose
    private String paymentType;

    @SerializedName("problem")
    @Expose
    private String problem;

    @SerializedName("entry_tim")
    @Expose
    private String entryTim;

    @SerializedName("call_log_status")
    @Expose
    private String callLogStatus;

    @SerializedName("call_log_transfer_id")
    @Expose
    private int callLogTransferId;

    @SerializedName("client_log_ip")
    @Expose
    private String clientLogIp;

    @SerializedName("client_log_timezone")
    @Expose
    private String clientLogTimezone;

    @SerializedName("rejected_reason")
    @Expose
    private String rejectedReason;

    @SerializedName("ref_delear")
    @Expose
    private int refDelear;

    public Logs(int callLogId, String callLogDate, int clientRefID, String callLogType, String clientName, String clientAddress, String clientPin, String clientMb, String clientEmail, int refCatId, int refServiceId, int callCompanyId, String productCompany, int amount, String paymentType, String problem, String entryTim, String callLogStatus, int callLogTransferId, String clientLogIp, String clientLogTimezone, String rejectedReason, int refDelear) {
        this.callLogId = callLogId;
        this.callLogDate = callLogDate;
        this.clientRefID = clientRefID;
        this.callLogType = callLogType;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.clientPin = clientPin;
        this.clientMb = clientMb;
        this.clientEmail = clientEmail;
        this.refCatId = refCatId;
        this.refServiceId = refServiceId;
        this.callCompanyId = callCompanyId;
        this.productCompany = productCompany;
        this.amount = amount;
        this.paymentType = paymentType;
        this.problem = problem;
        this.entryTim = entryTim;
        this.callLogStatus = callLogStatus;
        this.callLogTransferId = callLogTransferId;
        this.clientLogIp = clientLogIp;
        this.clientLogTimezone = clientLogTimezone;
        this.rejectedReason = rejectedReason;
        this.refDelear = refDelear;
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

    public int getClientRefID() {
        return clientRefID;
    }

    public void setClientRefID(int clientRefID) {
        this.clientRefID = clientRefID;
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

    public int getRefCatId() {
        return refCatId;
    }

    public void setRefCatId(int refCatId) {
        this.refCatId = refCatId;
    }

    public int getRefServiceId() {
        return refServiceId;
    }

    public void setRefServiceId(int refServiceId) {
        this.refServiceId = refServiceId;
    }

    public int getCallCompanyId() {
        return callCompanyId;
    }

    public void setCallCompanyId(int callCompanyId) {
        this.callCompanyId = callCompanyId;
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
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

    public String getEntryTim() {
        return entryTim;
    }

    public void setEntryTim(String entryTim) {
        this.entryTim = entryTim;
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

    public String getClientLogIp() {
        return clientLogIp;
    }

    public void setClientLogIp(String clientLogIp) {
        this.clientLogIp = clientLogIp;
    }

    public String getClientLogTimezone() {
        return clientLogTimezone;
    }

    public void setClientLogTimezone(String clientLogTimezone) {
        this.clientLogTimezone = clientLogTimezone;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public int getRefDelear() {
        return refDelear;
    }

    public void setRefDelear(int refDelear) {
        this.refDelear = refDelear;
    }
}
