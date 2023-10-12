package com.trodev.scanhub.models;

public class WIFIMModels {

    public  String security, net_name, pass, hidden_type, date , time, uid;

    public WIFIMModels() {
    }

    public WIFIMModels(String security, String net_name, String pass, String hidden_type, String date, String time, String uid) {
        this.security = security;
        this.net_name = net_name;
        this.pass = pass;
        this.hidden_type = hidden_type;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getNet_name() {
        return net_name;
    }

    public void setNet_name(String net_name) {
        this.net_name = net_name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHidden_type() {
        return hidden_type;
    }

    public void setHidden_type(String hidden_type) {
        this.hidden_type = hidden_type;
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
