package com.lncdriver.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PartnersResponce implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("partnerData")
    @Expose
    private ArrayList<PartnerList> partnerLists = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<PartnerList> getPartnerLists() {
        return partnerLists;
    }

    public void setPartnerLists(ArrayList<PartnerList> partnerLists) {
        this.partnerLists = partnerLists;
    }
}
