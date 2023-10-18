package com.trodev.scanhub.models;

public class URLModel {

    String url_name, url_link, date, time, uid;

    public URLModel() {
    }

    public URLModel(String url_name, String url_link, String date, String time, String uid) {
        this.url_name = url_name;
        this.url_link = url_link;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getUrl_name() {
        return url_name;
    }

    public void setUrl_name(String url_name) {
        this.url_name = url_name;
    }

    public String getUrl_link() {
        return url_link;
    }

    public void setUrl_link(String url_link) {
        this.url_link = url_link;
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
