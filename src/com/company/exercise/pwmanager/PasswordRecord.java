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
    public void print(){
        System.out.println("Site name: " + site);
        System.out.println("URL: " + url);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println();

    }
}
