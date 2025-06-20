package com.moroccantube.app.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[64]; // 512 bits for HS512
        secureRandom.nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated Base64 Key for HS512: " + base64Key);
    }
}