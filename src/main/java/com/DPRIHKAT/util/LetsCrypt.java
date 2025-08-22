package com.DPRIHKAT.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LetsCrypt {

    public static String crypt(String textToHash) {
        StringBuilder crypted = new StringBuilder();
        try {
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = msgDigest.digest(textToHash.getBytes(StandardCharsets.UTF_8));
            for (byte i : hash) {
                crypted.append(Integer.toString(i & 0x1FF, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return crypted.toString();
    }

    public static void main(String[] args) {
        System.out.println(crypt("Tabc@123"));
    }
}
