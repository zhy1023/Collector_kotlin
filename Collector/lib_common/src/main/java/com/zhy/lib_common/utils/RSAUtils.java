package com.zhy.lib_common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加密工具类
 */
public class RSAUtils {

    private static final String PUBLIC_PEM = "device_public.pem";

    private static final String sTransform = "RSA/ECB/PKCS1Padding";

    private static final String RSA = "RSA";

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodeKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param password 密码
     * @return
     */
    public static String encryptByPublic(String password, String publicKeyString) {
        if (TextUtils.isEmpty(password)) return "";
        try {
//            String pubKey = getPublicKeyFromAssets(context);
            byte[] pubKeyDecode = Base64.decode(publicKeyString, Base64.DEFAULT);
            String key = new String(pubKeyDecode);
            PublicKey publicKey = getPublicKeyFromX509(RSA, formatRSAPubulicKey(key));
            Cipher cipher = Cipher.getInstance(sTransform);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] plaintext = password.getBytes();
            byte[] output = cipher.doFinal(plaintext);
            return Base64.encodeToString(output, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String formatRSAPubulicKey(String publicKey) {
        if (TextUtils.isEmpty(publicKey)) return publicKey;
        String[] keys = publicKey.split("\n");
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            if (!key.startsWith("-----BEGIN") && !key.startsWith("-----END")) {
                builder.append(key);
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * 获取公钥
     *
     * @return
     */
    private static String getPublicKeyFromAssets(Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(PUBLIC_PEM));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder Result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                if (line.charAt(0) == '-') continue;
                Result.append(line);
            }
            return Result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
