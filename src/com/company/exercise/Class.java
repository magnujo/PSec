package com.company.exercise;

import java.math.BigInteger;

public class Class {
    public static void main(String[] args) {
        BigInteger p = new BigInteger("10");
        int pub_e = 7;
        int priv_e= 23;
        BigInteger mod = new BigInteger("187");
        BigInteger ciph = p.pow(pub_e).mod(mod);
        System.out.println("Ciphertext: " + ciph);
        BigInteger decr = ciph.pow(priv_e).mod(mod);
        System.out.println(decr);
    }
}
