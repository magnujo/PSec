package com.company.exercise;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

public class SignatureExercise {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(3072);
        KeyPair keyPair = generator.generateKeyPair();
        String message = "Hello world";
        byte[] input = message.getBytes(StandardCharsets.UTF_8);
        System.out.println(new String(input));

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Signature signer = Signature.getInstance("SHA256withRSA", "BC");
        signer.initSign(privateKey);

        signer.update(input);
        byte[] signature = signer.sign();
        Signature verifier = Signature.getInstance("SHA256withRSA", "BC");
        verifier.initVerify(publicKey);
        verifier.update(input);
        Boolean b = verifier.verify(signature);
        System.out.println(b);

        verifier.update("Helloo world".getBytes(StandardCharsets.UTF_8));
        System.out.println(verifier.verify(signature));



    }
}
