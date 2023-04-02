package chapter1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName fib3.java
 * @Description 内存方式的fib
 * @createTime 2023年03月01日 14:19:00
 * Tip: SuppressWarnings: https://www.cnblogs.com/fsjohnhuang/p/4040785.html
 */
@SuppressWarnings("boxing")
public class Fib3 {
    static Map<Integer, Integer> mem = new HashMap<Integer, Integer>(){
        {
            put(0, 0);
            put(1, 1);
        }
    };
    private static int fib3(int n){
        if(!mem.containsKey(n)){
            mem.put(n, fib3(n-1) + fib3(n-2));
        }
        return mem.get(n);
    }

    public static void main(String[] args) {
        System.out.println(mem);
        System.out.println(fib3(5));
        System.out.println(fib3(40));
    }
}

