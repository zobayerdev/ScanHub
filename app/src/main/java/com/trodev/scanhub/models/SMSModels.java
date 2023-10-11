package com.trodev.scanhub.models;

public class SMSModels {

    public  String sms, from, to, date, time, uid;

    public SMSModels() {
    }

    public SMSModels(String sms, String from, String to, String date, String time, String uid) {
        this.sms = sms;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
