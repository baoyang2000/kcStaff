package com.ctrl.android.kcetong.toolkit.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zzh on 2016/10/31.
 * 加密
 */

public class AESUtil {
    /**
     *
     * <p>
     * 方法名
     * </p>
     * 加密
     *
     * @param sSrc
     *            加密字符串
     * @param sKey
     *            加密密钥
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("alwi2hvnaz.s923k".getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return Base64.encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     *
     * <p>
     * 方法名
     * </p>
     * 解密
     *
     * @param sSrc
     *            解密文件
     * @param sKey
     *            解密密钥
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(
                    "alwi2hvnaz.s923k".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc);// 先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
        String cKey = ",[AjiEWohgew/.?|";
        // 需要加密的字串

        // 加密
        String enString = AESUtil.Encrypt("qqqqqqq", cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密

        String DeString = AESUtil.Decrypt("KaIun71Ls36d8RhabDekMw==", cKey);
        System.out.println("解密后的字串是：" + DeString);

    }
}
