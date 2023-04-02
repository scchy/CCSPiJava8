package chapter1;

import java.util.Stack;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Hanoi.java
 * @Description
 * @createTime 2023年03月01日 16:02:00
 */
public class Hanoi {
    private final int numDiscs;
    private final Stack<Integer> tA = new Stack<>();
    private final Stack<Integer> tB = new Stack<>();
    private final Stack<Integer> tC = new Stack<>();

    public Hanoi(int discs){
        this.numDiscs = discs;
        for(int i=1; i<=discs; i++){
            tA.push(i);
        }
    }

    private void move(Stack<Integer> begin, Stack<Integer> end, Stack<Integer> tmp, int n){
        if(n==1){
            end.push(begin.pop());
        }else{
            move(begin, tmp, end, n-1);
            move(begin, end, tmp, 1);
            move(tmp, end, begin, n-1);
        }
    }

    public void solve(){
        move(tA, tB, tC, numDiscs);
    }

    public static void main(String[] args){
        Hanoi hanoi = new Hanoi(3);
        hanoi.solve();
        System.out.println(hanoi.tA);
        System.out.println(hanoi.tB);
        System.out.println(hanoi.tC);
    }

}
