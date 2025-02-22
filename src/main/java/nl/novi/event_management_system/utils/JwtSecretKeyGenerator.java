package nl.novi.event_management_system.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(512);
        SecretKey secretKey = keyGen.generateKey();

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key: " + encodedKey);
    }
}