package com.trodev.scanhub.models;

public class User {

    public String uname, num, email, pass, uid;

    public User() {

    }

    public User(String uname, String num, String email, String pass) {
        this.uname = uname;
        this.num = num;
        this.email = email;
        this.pass = pass;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
