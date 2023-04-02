package chapter3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName MapColoringConstraint.java
 * @Description
 * @createTime 2023年03月06日 17:49:00
 */
public class MapColoringConstraint extends Constraint<String, String>{
    private String place1, place2;

    public MapColoringConstraint(String place1, String place2){
        // java 11
//        super(List.of(place1, place2));
        super(Arrays.asList(place1, place2));
        this.place1 = place1;
        this.place2 = place2;
    }

    @Override
    public boolean satisfied(Map<String, String> assignment){
        // 仅着色了一个
        if(!assignment.containsKey(place1) || !assignment.containsKey(place2)){
            return true;
        }
        // 两个地方着色不一样
        return !assignment.get(place1).equals(assignment.get(place2));
    }

    public static void main(String[] args){
        List<String> variables = Arrays.asList(
                "Western Australia", "Northern Territory",
                "South Australia", "Queensland", "New South Wales", "Victoria", "Tasmania"
        );
        Map<String, List<String>> domains = new HashMap<>();
        for(String var: variables){
            domains.put(var, Arrays.asList("red", "green", "blue"));
        }
        CSP<String, String> csp = new CSP<>(variables, domains);
        // 增加约束，相邻的区域颜色不能一致
        csp.addConstraint(new MapColoringConstraint("Western Australia", "Northern Territory"));
        csp.addConstraint(new MapColoringConstraint("Western Australia", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("South Australia", "Northern Territory"));
        csp.addConstraint(new MapColoringConstraint("Queensland", "Northern Territory"));
        csp.addConstraint(new MapColoringConstraint("Queensland", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("Queensland", "New South Wales"));
        csp.addConstraint(new MapColoringConstraint("New South Wales", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("Victoria", "South Australia"));
        csp.addConstraint(new MapColoringConstraint("Victoria", "New South Wales"));
        csp.addConstraint(new MapColoringConstraint("Victoria", "Tasmania"));
        Map<String, String> solution = csp.backtrackingSearch();
        if(solution == null){
            System.out.println("No solution found!");
        }else{
            System.out.println(solution);
        }
    }

}
