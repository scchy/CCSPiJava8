package chapter1;

import java.util.stream.IntStream;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Fib5.java
 * @Description
 * @createTime 2023年03月01日 14:43:00
 */
public class Fib5 {
    private int last=0, next=1;
    public IntStream stream(){
        return IntStream.generate(() -> {
          int oldLast = last;
          last = next;
          next = oldLast + next;
          return oldLast;
        });
    }

    public static void main(String[] args){
        Fib5 fib5 = new Fib5();
        fib5.stream().limit(10).forEachOrdered(System.out::println);
    }
}
