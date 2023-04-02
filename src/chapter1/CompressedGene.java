package chapter1;

import java.util.BitSet;
/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName CompressedGene.java
 * @Description 将 ACGT 核糖核酸基因序列进行压缩
 * @createTime 2023年03月01日 14:54:00
 */
public class CompressedGene {
    private BitSet bitSet;
    private int len;

    public CompressedGene(String gene){
        compress(gene);
    }

    private void compress(String gene){
        len = gene.length();
        bitSet = new BitSet(len * 2);
        final String upperGene = gene.toUpperCase();
        for(int i=0; i<len; i++){
            final int fLoc = 2 * i;
            final int sLoc = 2 * i + 1;
            switch (upperGene.charAt(i)){
                case 'A': // 00
                    bitSet.set(fLoc, false);
                    bitSet.set(sLoc, false);
                    break;
                case 'C': // 01
                    bitSet.set(fLoc, false);
                    bitSet.set(sLoc, true);
                    break;
                case 'G': // 10
                    bitSet.set(fLoc, true);
                    bitSet.set(sLoc, false);
                    break;
                case 'T': // 11
                    bitSet.set(fLoc, true);
                    bitSet.set(sLoc, true);
                    break;
                default:
                    throw new IllegalArgumentException("Can not Compress OUT of ACGT");
            }
        }
    }
    public String decompress() {
        if (bitSet == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(len);
        for(int i=0; i<len * 2; i+=2){
            final int fB = bitSet.get(i) ? 1: 0;
            final int sB = bitSet.get(i + 1) ? 1: 0;
            final int lastBits = fB << 1 | sB;
            switch (lastBits){
                case 0b00: // 00->A
                    builder.append('A');
                    break;
                case 0b01: // 01->C
                    builder.append('C');
                    break;
                case 0b10: // 10->G
                    builder.append('G');
                    break;
                case 0b11: // 11->T
                    builder.append('T');
                    break;
            }
        }
        return builder.toString();
    }
    public static void main(String[] args) {
        final String original = "TAGGGATTAACCGTTATATATATATAGCCATGGATC";
        CompressedGene compressed = new CompressedGene(original);
        final String decompressed = compressed.decompress();
        System.out.println(decompressed);
        System.out.println("original is the same as decompressed: " + original.equalsIgnoreCase(decompressed));
    }
}
