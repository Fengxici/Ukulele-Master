package timing.ukulele.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 常用加解密工具类
 */
public class EncryptUtil {

    //////////////////////////////////////////////BASE64////////////////////////////////////////////////////////
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();

    /**
     * data[]进行编码
     *
     * @param data 要加密的字节数据
     * @return 加密后的字符串
     */
    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    /**
     * 字符串惊醒加密
     *
     * @param data 要机密的字符串
     * @return 加密后的字符串
     */
    public static String encode(String data) {
        try {
            return encode(data.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密方法
     *
     * @param c 字符
     * @return 转换后的int值
     */
    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
    }

    /**
     * Decodes the given Base64 encoded String to a new byte array. The byte
     * array holding the decoded data is returned.
     *
     * @param s 要解密的字符串
     * @return 字节数组
     */
    public static byte[] decode(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12)
                    + (decode(s.charAt(i + 2)) << 6) + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }

    /**
     * data[]进行编码
     *
     * @param data 要加密的字节数组
     * @return 加密后的字符串
     */
    public static String Base64Encoder(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                // 不需要空格
                n = 0;
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }
    ////////////////////////////////////////////BASE64////////////////////////////////////////////////////////////

    ////////////////////////////////////////////MD5////////////////////////////////////////////////////////////
    public static String getMD5(String source) {
        String s = null;
        char hexChar[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());// 使用指定的byte数组更新摘要
            byte[] hashCalc = md.digest();// 完成哈希计算
            char result[] = new char[16 * 2];// MD5结果返回的是32位字符串，每位是16进制表示的
            int k = 0;
            for (int i = 0; i < 16; i++) {// 循环16次，对每个字节进行操作转换
                byte everyByte = hashCalc[i];
                result[k++] = hexChar[everyByte >>> 4 & 0xf];// 对每个字节的高4位进行处理，逻辑右移，再相与
                result[k++] = hexChar[everyByte & 0xf];// 低4位转换
            }
            s = new String(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return s;
    }
    ////////////////////////////////////////////MD5////////////////////////////////////////////////////////////

    ////////////////////////////////////////////SHA////////////////////////////////////////////////////////////
    /**
     * 算法种类
     */
    private final static String KEY_SHA1 = "SHA-1";
    private final static String KEY_SHA256 = "SHA-256";
    private final static String KEY_SHA512 = "SHA-512";

    public static String encBySha1(String data) throws Exception {
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA1);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    public static String encBySha256(String data) throws Exception {
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA256);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    public static String encBySha512(String data) throws Exception {
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA512);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    private static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
    ////////////////////////////////////////////SHA////////////////////////////////////////////////////////////

    ////////////////////////////////////////////DES,3DES,AES//////////////////////////////////////////////////
    private final static String KEY_DES = "DES";
    private static final String KEY_3_DES = "DESede";
    private final static String KEY_AES = "AES"; // 测试
    //长度8字节
    private final static String DES_KEY = "12345678";
    //长度24字节
    private final static String DES_3_KEY = "213456781234567812345678";
    //长度16，24，32字节
    private final static String AES_KEY = "1234567812345678";

    private static byte[] decByDes(byte[] data) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(DES_KEY.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DES);
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance(KEY_DES);
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        return cipher.doFinal(data);
    }

    private static byte[] encByDes(byte[] data) throws Exception {
        DESKeySpec desKey = new DESKeySpec(DES_KEY.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DES);
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance(KEY_DES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        return cipher.doFinal(data);
    }

    private static byte[] decBy3Des(byte[] data) throws Exception {
        SecureRandom random = new SecureRandom();
        SecretKey deskey = new SecretKeySpec(DES_3_KEY.getBytes(), KEY_3_DES);
        Cipher cipher = Cipher.getInstance(KEY_3_DES);
        cipher.init(Cipher.DECRYPT_MODE, deskey, random);
        return cipher.doFinal(data);
    }

    private static byte[] encBy3Des(byte[] data) throws Exception {
        SecureRandom random = new SecureRandom();
        SecretKey deskey = new SecretKeySpec(DES_3_KEY.getBytes(), KEY_3_DES);
        Cipher cipher = Cipher.getInstance(KEY_3_DES);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
        return cipher.doFinal(data);
    }

    private static byte[] decByAes(byte[] data) throws Exception {
        SecretKey deskey = new SecretKeySpec(AES_KEY.getBytes(), KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }

    private static byte[] encByAes(byte[] data) throws Exception {
        SecretKey deskey = new SecretKeySpec(AES_KEY.getBytes(), KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }
    ////////////////////////////////////////////DES,3DES,AES//////////////////////////////////////////////////

    ////////////////////////////////////////////RSA///////////////////////////////////////////////////////////
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    private static final int keyLen = 2048;// 密钥长度

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = keyLen / 8 - 11;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = keyLen / 8;

    /**
     * genKeyPair:(). <br/>
     * <p>
     * 生成公司钥对，返回的是经过BASE64编码的密钥
     *
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keyLen);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * sign:(). <br/>
     * <p>
     * 用私钥进行签名，返回BASE64编码的内容
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return encode(signature.sign());
    }

    /**
     * verify:(). <br/>
     * <p>
     * 用公钥对签名进行验证
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(decode(sign));
    }

    /**
     * decryptByPrivateKey:(). <br/>
     * <p>
     * 私钥解密
     *
     * @param encryptedData
     * @param privateKey
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * decryptByPublicKey:(). <br/>
     * <p>
     * 公钥解密
     *
     * @param encryptedData
     * @param publicKey
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",new org.bouncycastle.jce.provider.BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * encryptByPublicKey:(). <br/>
     * <p>
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * encryptByPrivateKey:(). <br/>
     * <p>
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * getPrivateKey:(). <br/>
     * <p>
     * 获取BASE64编码的私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encode(key.getEncoded());
    }

    /**
     * getPublicKey:(). <br/>
     * <p>
     * 获取BASE64编码的公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     * @author chiwei
     * @since JDK 1.6
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encode(key.getEncoded());
    }
    ////////////////////////////////////////////RSA///////////////////////////////////////////////////////////


    public static void main(String[] args) {
        System.out.println(encode("tree"));
        System.out.println(new String(decode(encode("tree"))));
        System.out.println(getMD5("tree"));
        String key = "123";
        try {
            System.out.println(encBySha1(key));
            System.out.println(encBySha256(key));
            System.out.println(encBySha512(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("DES密钥:\n" + DES_KEY);
            System.out.println("DES密钥字节长度:\n" + DES_KEY.getBytes().length);
            String word = "123";
            System.out.println("原文：" + word);
            System.out.println("=============DES=============");
            byte b[] = encByDes(word.getBytes());
            String encWord = new String(b);
            System.out.println("加密后：" + encWord);
            System.out.println("解密后：" + new String(decByDes(b)));
            System.out.println("=============3DES=============");
            System.out.println("3DES密钥:" + DES_3_KEY);
            System.out.println("3DES密钥字节长度:" + DES_3_KEY.getBytes().length);
            b = encBy3Des(word.getBytes());
            encWord = new String(b);
            System.out.println("加密后：" + encWord);
            System.out.println("解密后：" + new String(decBy3Des(b)));
            System.out.println("=============AES=============");
            System.out.println("AES密钥:" + AES_KEY);
            System.out.println("AES密钥字节长度:" + AES_KEY.getBytes().length);
            b = encByAes(word.getBytes());
            encWord = new String(b);
            System.out.println("加密后：" + encWord);
            System.out.println("解密后：" + new String(decByAes(b)));
        } catch (Exception e) {
            e.printStackTrace();
        }
       String json = "[{\"ZYH\":\"546786\",\"ZYHM\":\"657566\",\"WDHM\":\"5467\",\"XM\":\"eteryuytr\",\"XB\":\"45678i7654\",\"DQBQ\":\"5678\",\"DQCW\":\"ertyu\"}]";
        System.out.println(json);
        try {
            Map keyMap = genKeyPair();
            String publicKey = getPublicKey(keyMap);
            String privateKey = getPrivateKey(keyMap);
            System.out.println("公钥-->" + publicKey);
            System.out.println("私钥-->" + privateKey);
            System.out.println("待加密的字符串：" + json);
            byte[] enBy = encryptByPublicKey(json.getBytes(), publicKey);
            String enStr = new String(enBy, "UTF-8");
            System.out.println("加密后：" + enStr);
            String enEncode = encode(enBy);
            System.out.println("加密后编码内容：" + enEncode);
            byte[] deBy = decryptByPrivateKey(decode(enEncode), privateKey);
            System.out.println("解密后：" + new String(deBy));
            System.out.println("解密后编码内容：" + encode(deBy));
            String sign = sign(enBy, privateKey);
            System.out.println("签名：" + sign);
            System.out.println("对签名验证：" + verify(enBy, publicKey, sign));
            System.out.println("BASE64编码密文摘要：" + getMD5(enEncode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
