package com.trodev.scanhub.models;

public class EmailModel {

   public String email_from, email_to, email_sms, date, time, uid;

    public EmailModel() {
    }

    public EmailModel(String email_from, String email_to, String email_sms, String date, String time, String uid) {
        this.email_from = email_from;
        this.email_to = email_to;
        this.email_sms = email_sms;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getEmail_from() {
        return email_from;
    }

    public void setEmail_from(String email_from) {
        this.email_from = email_from;
    }

    public String getEmail_to() {
        return email_to;
    }

    public void setEmail_to(String email_to) {
        this.email_to = email_to;
    }

    public String getEmail_sms() {
        return email_sms;
    }

    public void setEmail_sms(String email_sms) {
        this.email_sms = email_sms;
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
