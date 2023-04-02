package chapter2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Gene.java
 * @Description
 * @createTime 2023年03月02日 10:24:00
 */
public class Gene {
    public enum Nucleotide {
        A, C, G, T;
    }

    public static class Codon implements Comparable<Codon> {
        public final Nucleotide first, second, third;
        private final Comparator<Codon> comparator = Comparator.comparing((Codon C) -> C.first)
                        .thenComparing((Codon C) -> C.second)
                        .thenComparing((Codon C) -> C.third);
        public Codon(String codonStr){
            first = Nucleotide.valueOf(codonStr.substring(0, 1));
            second = Nucleotide.valueOf(codonStr.substring(1, 2));
            third = Nucleotide.valueOf(codonStr.substring(2, 3));
        }
        @Override
        public int compareTo(Codon other){
            return comparator.compare(this, other);
        }
    }

    private final ArrayList<Codon> codons = new ArrayList<>();

    public Gene(String geneStr){
        for(int i=0; i < geneStr.length() - 3; i += 3){
            codons.add(new Codon(geneStr.substring(i, i+3)));
        }
    }

    // 类似python 中的any
    public boolean linearContains(Codon key){
        for(Codon codon: codons){
            if(codon.compareTo(key) == 0){
                return true;
            }
        }
        return false;
    }

    public boolean binaryContains(Codon key){
        ArrayList<Codon> sortedCodons = new ArrayList<>(codons);
        Collections.sort(sortedCodons);
        int low = 0;
        int high = sortedCodons.size() - 1;
        while ( low < high){
            int mid = (low + high) / 2;
            int cmp = sortedCodons.get(mid).compareTo(key);
            if (cmp < 0){
                low = mid +1;
            }else if (cmp > 0){
                high = mid -1;
            }else{
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args){
        String geneStr = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT";
        // 基因段
        Gene myGene = new Gene(geneStr);
        // 一个密码子
        Codon acg = new Codon("ACG");
        Codon gat = new Codon("GAT");
        Codon gat2 = new Codon("TAC");
        // 从左到右 哪个位置不同，返回第一个元素不同的差值
        System.out.println( acg.compareTo(gat) );
        System.out.println( acg.compareTo(gat2) );
        System.out.println(myGene.linearContains(acg));
        System.out.println(myGene.linearContains(gat));
        System.out.println(myGene.binaryContains(acg));
        System.out.println(myGene.binaryContains(gat));
    }

}
