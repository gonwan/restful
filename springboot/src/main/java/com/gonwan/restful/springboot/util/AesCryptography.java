package com.gonwan.restful.springboot.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

/**
 * JCP policy files required to work.
 * @author binson.qian
 */
@Component
public class AesCryptography {

    private static final char[] PASSWORD = "gonwan".toCharArray();
    private static final byte[] SALT = new byte[] { 0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 };
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_STRENGTH = 384;
    private SecretKeySpec keySpec;
    private AlgorithmParameterSpec parameterSpec;

    /* avoid installing jce policy files. */
    static {
        try {
            Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            Field isRestricted = jceSecurity.getDeclaredField("isRestricted");
            /* JDK 8u102 changed this field to final */
            if (Modifier.isFinal(isRestricted.getModifiers())) {
                Field modifiers = Field.class.getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.setInt(isRestricted, isRestricted.getModifiers() & ~Modifier.FINAL);
            }
            isRestricted.setAccessible(true);
            isRestricted.setBoolean(null, Boolean.FALSE);
            isRestricted.setAccessible(false);
        } catch (Exception e) {
            /* ignore */
        }
    }

    public AesCryptography() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(PASSWORD, SALT, ITERATION_COUNT, KEY_STRENGTH);
            SecretKey secretKey = factory.generateSecret(spec);
            byte[] key = new byte[32];
            byte[] iv = new byte[16];
            System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
            System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);
            keySpec = new SecretKeySpec(key, "AES");
            parameterSpec = new IvParameterSpec(iv);
        } catch (GeneralSecurityException e) {
            /* ignore */
        }
    }

    public String Encrypt(String clearText) throws GeneralSecurityException {
        if (clearText == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
        byte[] aesEncryptedData = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_16LE));
        String base64EncryptedData = Base64.getEncoder().encodeToString(aesEncryptedData);
        return base64EncryptedData;
    }

    public String Decrypt(String cipherText) throws GeneralSecurityException {
        if (cipherText == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
        byte[] base64DecryptedData = Base64.getDecoder().decode(cipherText);
        byte[] aesDecryptedData = cipher.doFinal(base64DecryptedData);
        return new String(aesDecryptedData, StandardCharsets.UTF_16LE);
    }

    public static void main(String[] args) throws GeneralSecurityException {
        String s = "123456";
        AesCryptography aes = new AesCryptography();
        String s2 = aes.Encrypt(s);
        String s3 = aes.Decrypt(s2);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
    }

}
