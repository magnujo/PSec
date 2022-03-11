package com.company.exercise.pwmanager;

import java.io.Serializable;
class PasswordRecord implements Serializable {

    String site, url, username, password;

    PasswordRecord(String site, String url, String username, String password) {
        this.site = site;
        this.url = url;
        this.username = username;
        this.password = password;
    }
    //Methods?
    public void printLabels(){

    }
}
