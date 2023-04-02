package chapter1;

import java.util.Random;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName UnbreakableEncryption.java
 * @Description
 * @createTime 2023年03月01日 15:45:00
 */
public class UnbreakableEncryption {
    private static byte[] randomKey(int len){
        byte[] dummy = new byte[len];
        Random rnd = new Random();
        rnd.nextBytes(dummy);
        return dummy;
    }

    public static keyPair encrypt(String org){
        byte[] orgBytes = org.getBytes();
        byte[] dummyKey = randomKey(orgBytes.length);
        byte[] encryptedKey = new byte[orgBytes.length];
        for(int i=0; i<orgBytes.length; i++){
            // enc = org 异或 dummyKey
            encryptedKey[i] = (byte) (orgBytes[i] ^ dummyKey[i]);
        }
        return new keyPair(dummyKey, encryptedKey);
    }

    public static String decrypt(keyPair kp){
        byte[] decrypted = new byte[kp.key1.length];
        for(int i = 0;i < kp.key1.length; i++){
            // dec = enc 异或 dummyKey
            decrypted[i] = (byte) (kp.key1[i] ^ kp.key2[i]);
        }
        return new String(decrypted);
    }

    public static void main(String[] args){
        keyPair kp = encrypt("One Time Pad!");
        System.out.println(kp.key1);
        System.out.println(kp.key2);
        String res = decrypt(kp);
        System.out.println(res);
    }
}
