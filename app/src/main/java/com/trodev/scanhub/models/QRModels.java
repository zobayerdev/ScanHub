package com.trodev.scanhub.models;

public class QRModels {

    public  String make_date, expire_date, company_name, product_name,  product_info, date, time,uid;

    public QRModels() {
    }

    public QRModels(String make_date, String expire_date, String company_name, String product_name, String product_info, String date, String time, String uid) {
        this.make_date = make_date;
        this.expire_date = expire_date;
        this.company_name = company_name;
        this.product_name = product_name;
        this.product_info = product_info;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getMake_date() {
        return make_date;
    }

    public void setMake_date(String make_date) {
        this.make_date = make_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
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
