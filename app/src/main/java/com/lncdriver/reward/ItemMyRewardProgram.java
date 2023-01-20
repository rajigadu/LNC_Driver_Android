package com.lncdriver.reward;

import java.io.Serializable;

public class ItemMyRewardProgram implements Serializable {

    String id = "";
    String pname = "";
    String pdescription = "";
    String note = "";
    String target = "";
    String bonus = "";
    String status1 = "";
    String craeted_date = "";
    String total = "";
    String driver_time = "";

    public String getDriver_time() {
        return driver_time;
    }

    public void setDriver_time(String driver_time) {
        this.driver_time = driver_time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getCraeted_date() {
        return craeted_date;
    }

    public void setCraeted_date(String craeted_date) {
        this.craeted_date = craeted_date;
    }
}