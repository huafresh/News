package com.example.hua.framework.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具。支持AES加密和RSA加密
 *
 * @author hua
 * @version 2017/10/21 13:21
 */

public class EncryptUtil {

    /**
     * 根据字符串生成AES加密密钥。
     *
     * @param key 用来生成密钥的字符串
     * @return AES加密密钥
     */
    private static Key generateAESKey(String key) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            sr.setSeed(key.getBytes());
            keyGenerator.init(128, sr);
            SecretKey secretKey = keyGenerator.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用AES对字符串进行加密
     *
     * @param content 要加密的字符串
     * @param key     用来生成AES密钥
     * @return 加密后的字符串
     */
    public static String encryptByAES(String content, String key) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            byte[] contentByte = content.getBytes("utf-8");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, generateAESKey(key));
            return Base64.encodeToString(cipher.doFinal(contentByte), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对字符串进行AES解密
     *
     * @param content 要解密的内容
     * @param key     用来生成密钥
     * @return 解密后的字符串
     */
    public static String decryptByAES(String content, String key) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, generateAESKey(key));
            return new String(cipher.doFinal(Base64.decode(content, Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用RSA公钥或者私钥加密
     *
     * @param content 要加密的字符串
     * @param key     公钥或者私钥。可以使用方法{@link #loadPrivateKeyFromPem(InputStream)}
     *                或者{@link #loadPublicKeyFromPem(InputStream)}从pem文件中加载公钥或者私钥
     * @return 加密后的字符串
     */
    public static String encryptByRSAKey(String content, Key key) {
        if (TextUtils.isEmpty(content) || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(content.getBytes());
            return Base64.encodeToString(result, Base64.DEFAULT).replaceAll("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用RSA公钥或者私钥解密
     *
     * @param content 要解密的字符串
     * @param key     公钥或者私钥。可以使用方法{@link #loadPrivateKeyFromPem(InputStream)}
     *                或者{@link #loadPublicKeyFromPem(InputStream)}从pem文件中加载公钥或者私钥
     * @return 解密后的字符串
     */
    public static String decryptByRSAKey(String content, Key key) {
        if (TextUtils.isEmpty(content) || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析.pem文件得到PublicKey对象
     *
     * @param in .pem文件输入流。方法执行完会自动关闭
     * @return PublicKey对象
     */
    public static PublicKey loadPublicKeyFromPem(InputStream in) {
        if (in == null) {
            return null;
        }

        try {
            //先读取公钥字符串
            String publicString = readPemToString(in);
            //根据公钥字符串生成PublicKey对象
            byte[] decode = Base64.decode(publicString, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 解析.pem文件得到PublicKey对象
     *
     * @param in .pem文件输入流。方法执行完会自动关闭
     * @return PublicKey对象
     */
    public static PrivateKey loadPrivateKeyFromPem(InputStream in) {
        if (in == null) {
            return null;
        }

        try {
            //先读取私钥字符串
            String privateString = readPemToString(in);

            //根据私钥字符串生成PrivateKey对象
            byte[] decode = Base64.decode(privateString, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @NonNull
    private static String readPemToString(InputStream in) throws IOException{
        StringBuilder builder = new StringBuilder();
        BufferedReader buffIn = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = buffIn.readLine()) != null) {
            if (line.startsWith("-")) {
                //忽略pem文件的注释内容
                continue;
            }
            builder.append(line);
        }
        return builder.toString();
    }

}
