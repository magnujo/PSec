package com.company.exercise.pwmanager;

import java.util.ArrayList;
class PasswordTable extends ArrayList<PasswordRecord> {

    void print() {
        System.out.println("The table contains the following " + size() + " records:");
        System.out.println();
        for (int i=0; i<size() ; i++) {
            get(i).print();
        }
        System.out.println();
    }


}
