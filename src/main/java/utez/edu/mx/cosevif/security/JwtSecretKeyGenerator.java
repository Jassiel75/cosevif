package utez.edu.mx.cosevif.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtSecretKeyGenerator {

    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        String encodedKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Secret Key: " + encodedKey);
    }
}
