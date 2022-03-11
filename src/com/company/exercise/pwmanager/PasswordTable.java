package com.company.exercise.pwmanager;

import java.util.ArrayList;
class PasswordTable extends ArrayList<PasswordRecord> {

    void print() {
        PasswordRecord.printLabels();
        for (int i=0; i<size() ; i++) get(i).print();
        System.out.println();
    }
}
