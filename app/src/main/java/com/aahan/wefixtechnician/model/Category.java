package com.aahan.wefixtechnician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("tbl_category_id")
    @Expose
    private int tbl_category_id;
    @SerializedName("tbl_category_name")
    @Expose
    private String tbl_category_name;
    @SerializedName("tbl_category_image")
    @Expose
    private String tbl_category_image;
    @SerializedName("status")
    @Expose
    private String status;

    public Category(int tbl_category_id, String tbl_category_name, String tbl_category_image, String status) {
        this.tbl_category_name = tbl_category_name;
        this.tbl_category_image = tbl_category_image;
        this.status = status;
        this.tbl_category_id = tbl_category_id;
    }

    public int getTbl_category_id() {
        return tbl_category_id;
    }

    public void setTbl_category_id(int tbl_category_id) {
        this.tbl_category_id = tbl_category_id;
    }

    public String getTbl_category_name() {
        return tbl_category_name;
    }

    public void setTbl_category_name(String tbl_category_name) {
        this.tbl_category_name = tbl_category_name;
    }

    public String getTbl_category_image() {
        return tbl_category_image;
    }

    public void setTbl_category_image(String tbl_category_image) {
        this.tbl_category_image = tbl_category_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
