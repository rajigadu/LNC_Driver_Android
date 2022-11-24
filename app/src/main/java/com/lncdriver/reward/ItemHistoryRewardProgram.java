package com.lncdriver.reward;

import java.io.Serializable;

public class ItemHistoryRewardProgram implements Serializable {

    String pickup_address = "";
    String drop_address = "";
    String otherdate = "";
    String time = "";
    String distance = "";
    String point = "";




    public String getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(String pickup_address) {
        this.pickup_address = pickup_address;
    }

    public String getDrop_address() {
        return drop_address;
    }

    public void setDrop_address(String drop_address) {
        this.drop_address = drop_address;
    }

    public String getOtherdate() {
        return otherdate;
    }

    public void setOtherdate(String otherdate) {
        this.otherdate = otherdate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
