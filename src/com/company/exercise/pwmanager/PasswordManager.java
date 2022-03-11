package com.company.exercise.pwmanager;

class PasswordManager {
    static String dir = "/Users/nielsj/Desktop/dev/java/keepassxc";
    static String passwordFileName = dir + "/encryptedPasswordTable";
    public static void main(String args[]) {

        // reading password manager table from file
        System.out.println("Reading password manager from file:");
        PasswordProtector pp = new PasswordProtector(passwordFileName);
        PasswordTable pt = pp.load();
        pt.print();

        // deleting all table records
        System.out.println("Deleting all records from table:");
        pt.clear();
        pt.print();

        // adding records agaig
        System.out.println("Adding two passwords records:");
        pt.add(new PasswordRecord("Facebook", "facebook.com", "fb_user", "fb_pw"));
        pt.add(new PasswordRecord("YouTube", "youtube.com", "yt_user", "yt_pw"));
        pt.print();
        // storing table
        System.out.println("Storing password table");
        pp.store(pt);
    }
}
