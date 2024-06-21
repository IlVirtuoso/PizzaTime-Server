package com.pizzaidph2.pizzaidph2.Component;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PWDUtilities {

    public boolean checkPasswordPolicy(String p){
        String pwd = p.trim();
        if(pwd.length()>=6){
            return true;
        }
        return false;
    }

    public String sha256Encoding(String pwd) {
        pwd = pwd.trim();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("FAILING IN CREATE PWD SHA 256: NoSuchAlgorithmException");
            return null;
        }
        byte[] encodedHash = digest.digest(pwd.getBytes());
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }



}
