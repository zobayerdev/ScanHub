package com.trodev.scanhub.models;

public class LocationModel {

    String loc_from, loc_to, loc_sms, date, time, uid;

    public LocationModel() {
    }

    public LocationModel(String loc_from, String loc_to, String loc_sms, String date, String time, String uid) {
        this.loc_from = loc_from;
        this.loc_to = loc_to;
        this.loc_sms = loc_sms;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getLoc_from() {
        return loc_from;
    }

    public void setLoc_from(String loc_from) {
        this.loc_from = loc_from;
    }

    public String getLoc_to() {
        return loc_to;
    }

    public void setLoc_to(String loc_to) {
        this.loc_to = loc_to;
    }

    public String getLoc_sms() {
        return loc_sms;
    }

    public void setLoc_sms(String loc_sms) {
        this.loc_sms = loc_sms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
