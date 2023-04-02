package chapter1;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName PiCalculator.java
 * @Description: pi = 4/1 - 4/3 + 4/5 - 4/7 ...
 * @createTime 2023年03月01日 15:57:00
 */
public class PiCalculator {
    public static double calculatePi(int nTerms){
        final double num = 4.0;
        double denominator = 1.0;
        double op = 1.0;
        double pi = 0.0;
        for (int i=0; i < nTerms; i++){
            pi += op * (num / denominator);
            denominator += 2;
            op *= -1.0;
        }
        return pi;
    }

    public static void main(String[] args) {
        System.out.println(calculatePi(1000000));
    }
}
