package chapter3;

import java.util.*;
import java.util.Map.Entry;
/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName QueensConstraint.java
 * @Description
 * @createTime 2023年03月07日 15:41:00
 */
public class QueensConstraint extends  Constraint<Integer, Integer> {
    private List<Integer> columns;

    public QueensConstraint(List<Integer> columns){
        super(columns);
        this.columns = columns;
    }

    @Override
    public boolean satisfied(Map<Integer, Integer> assignment){
        for( Entry<Integer, Integer> item: assignment.entrySet() ){
            int q1c = item.getKey();
            int q1r = item.getValue();
            for(int q2c = q1c + 1; q2c <= columns.size(); q2c++){
                if(assignment.containsKey(q2c)){
                    int q2r = assignment.get(q2c);
                    // 同一行
                    if(q1r == q2r){
                        return false;
                    }
                    // 同diagonal
                    if(Math.abs(q1r - q2r) == Math.abs(q1c - q2c)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args){
        List<Integer> columns = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Map<Integer, List<Integer>> rows = new HashMap<>();
        for(int col: columns){
            rows.put(col, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        }
        // 邻接表
        CSP<Integer, Integer> csp = new CSP<>(columns, rows);
        csp.addConstraint(new QueensConstraint(columns));
        Map<Integer, Integer> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            System.out.println(solution);
        }
    }
}

