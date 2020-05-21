package com.mao.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密解密
 * @author : create by zongx at 2020/5/7 14:43
 */
public class SecretUtil {

    private static final String ALGORITHM = "SHA1PRNG";

    /**
     * 加密
     * @param data byte数据
     * @param key 密钥
     * @param secretEnum 加密类型
     * @return 加密后的byte数据
     */
    public static byte[] encode(byte[] data, String key, SecretEnum secretEnum){
        try{
            if (null == data || data.length <= 0) return null;
            Cipher cipher = getCipher(key,secretEnum, Cipher.ENCRYPT_MODE);
            return cipher.doFinal(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param data byte数据
     * @param key 密钥
     * @param secretEnum 解密类型
     * @return 解密后的数据
     */
    public static byte[] decode(byte[] data, String key, SecretEnum secretEnum){
        try{
            if (null == data || data.length <= 0) return null;
            Cipher cipher = getCipher(key, secretEnum,Cipher.DECRYPT_MODE);
            return cipher.doFinal(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param data 需要加密的数据
     * @param key 密钥
     * @param secretEnum 加密类型
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key, SecretEnum secretEnum) {
        try {
            if (SU.isEmpty(data) || SU.isEmpty(key)) return null;
            byte[] content = data.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = getCipher(key,secretEnum, Cipher.ENCRYPT_MODE);
            byte[] result = cipher.doFinal(content);
            return parseByte2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param data 需要解密的数据
     * @param key 密钥
     * @param secretEnum 加密类型
     * @return 解密数据
     */
    public static String decrypt(String data, String key, SecretEnum secretEnum) {
        try {
            if (SU.isEmpty(data) || SU.isEmpty(key)) return null;
            byte[] content = parseHexStr2Byte(data);
            if (null == content) return null;
            Cipher cipher = getCipher(key, secretEnum,Cipher.DECRYPT_MODE);
            byte[] result = cipher.doFinal(content);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Cipher getCipher(String key, SecretEnum secretEnum, int mode) throws Exception{
        KeyGenerator k_gen = KeyGenerator.getInstance(secretEnum.getType());
        SecureRandom secureRandom = SecureRandom.getInstance(ALGORITHM);
        secureRandom.setSeed(key.getBytes());
        k_gen.init(secretEnum.getStrong(), secureRandom);
        SecretKey secretKey = k_gen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, secretEnum.getType());
        Cipher cipher = Cipher.getInstance(secretEnum.getType());// 创建密码器
        cipher.init(mode, keySpec);// 初始化
        return cipher;
    }

    /**
     * 将二进制转换成16进制
     * @param buf 二进制数据
     * @return String
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制转二进制
     * @param hexStr 16进制数据
     * @return 二进制数组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    @Getter
    @AllArgsConstructor
    public enum SecretEnum {

        AES("AES",128),
        DES("DES",56);

        private String type;
        private int strong;

    }

    /**
     * MD5算法类型
     */
    private static final String KEY_MD5 = "MD5";

    /**
     * SHA-1算法类型
     */
    private static final String KEY_SHA = "SHA";

    /**
     * MD5加密：Message-Digest Algorithm(信息摘要算法)
     * 单向加密；生成固定长度加密值（十六进制字符串）
     * MD5加密的特点：
     * 1.压缩性：任意长度的数据，算出的MD5值长度是固定的；
     * 2.易计算：从原始数据计算出MD5值很容易；
     * 3.抗修改性：原始数据作出任意改动，得出的MD5值的差别很大；
     * 4.弱抗碰撞：已知原始数据和MD5值，找到一个具有相同MD5值得数据是非常困难的；
     * 5.强抗碰撞：找2个不同的原始数据，使它们具有相同的MD5值是非常困难的；
     * @param str str
     */
    public static String MD5(String str){
        //md5
        return encrypt(str,KEY_MD5);
    }

    /**
     * SHA加密：Secure Hash Algorithm（安全散列算法）
     * SHA算法被广泛应用到电子商务信息安全领域，虽然SHA和MD5都通过碰撞法破解了，
     * 但SHA任然是公认的安全加密算法，较MD5更安全。
     * SHA-1与MD5的区别：
     * 1.二者都由MD4导出，二者很类似。因此二者的强度和其它特性都很类似。
     * 2.SHA-1的摘要长度要比MD5长32位，使用强行技术，产生任何一个报文要等于给定报文摘要的难度对
     *   MD5是2^128数量级的操作，而对SHA-1是2^160数量级的操作，因此对SHA-1强行攻击的难度更大。
     * 3.相同硬件上，SHA-1的运算速度比MD5慢。
     * @param str String
     * @return String
     */
    public static String SHA(String str){
        //sha1 as same
        return encrypt(str,KEY_SHA);
    }

    /**
     * 加密方法：MD5和SHA加密方法相同，MessageDigest类中已分配好
     * @param str String
     * @param type 加密类型
     * @return String
     */
    private static String encrypt(String str, String type){
        byte[] bytes = new byte[0];
        try{
            bytes = MessageDigest.getInstance(type).digest(str.getBytes());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return new BigInteger(1,bytes).toString(16);
    }

    public static void main(String[] args) {

    }

}
