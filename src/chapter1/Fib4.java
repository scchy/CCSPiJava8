package chapter1;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Fib4.java
 * @Description
 * @createTime 2023年03月01日 14:41:00
 */
public class Fib4 {
    private static int fib4(int n){
        int last=0, next=1;
        for(int i = 0;i< n; i++){
            int oldLast = last;
            last = next;
            next = oldLast + next;
        }
        return last;
    }

    public static void main(String[] args) {
        System.out.println(fib4(10));
        System.out.println(fib4(40));
    }
}
