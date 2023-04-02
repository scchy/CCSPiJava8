package chapter1;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName keyPair.java
 * @Description
 * @createTime 2023年03月01日 15:42:00
 */
public class keyPair {
    public final byte[] key1;
    public final byte[] key2;

    keyPair(byte[] k1, byte[] k2){
        this.key1 = k1;
        this.key2 = k2;
    }

}
