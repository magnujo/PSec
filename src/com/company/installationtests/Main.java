package com.company.installationtests;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        System.out.println(System.currentTimeMillis());
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(3072);
        KeyPair keyPair = generator.generateKeyPair();

        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println(System.currentTimeMillis());

        System.out.println("Private exponent:");
        System.out.println(rsaPrivateKey.getPrivateExponent());

        System.out.println("Private mod: ");
        System.out.println(rsaPrivateKey.getModulus());

        System.out.println("Public exponent: ");
        System.out.println(rsaPublicKey.getPublicExponent());

        System.out.println("Public mod:");
        System.out.println(rsaPublicKey.getModulus());





    }
}

