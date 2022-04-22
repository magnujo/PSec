package com.company.exercise;

import java.util.regex.Pattern;

public class ValidateURL {

    public static void main(String[] args) {
        ValidateURL validateURL = new ValidateURL();
        System.out.println(validateURL.validateStep2("ruc_17.dk/search(***)"));
    }

    private boolean validateStep2(String url) {
        boolean b = false;
        if (Pattern.matches("[a-zA-Z.0-9_]*", url)) b = true;
        return b;
    }
}
