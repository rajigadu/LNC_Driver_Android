package com.lncdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Week implements Serializable {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("effectiveCreditDate")
    @Expose
    private String effectiveCreditDate;
    @SerializedName("effectiveDebitDate")
    @Expose
    private String effectiveDebitDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;

    @SerializedName("from_date")
    @Expose
    private String fromDate;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getEffectiveCreditDate() {
        return effectiveCreditDate;
    }

    public void setEffectiveCreditDate(String effectiveCreditDate) {
        this.effectiveCreditDate = effectiveCreditDate;
    }

    public String getEffectiveDebitDate() {
        return effectiveDebitDate;
    }

    public void setEffectiveDebitDate(String effectiveDebitDate) {
        this.effectiveDebitDate = effectiveDebitDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}

