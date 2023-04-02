package chapter3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName CSP.java
 * @Description
 * @createTime 2023年03月06日 17:07:00
 */
public class CSP<V, D> {
    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints = new HashMap<>();

    public CSP(List<V> variables, Map<V, List<D>> domains){
        this.variables = variables;
        this.domains = domains;
        for(V var: variables){
            constraints.put(var, new ArrayList<>());
            if(!domains.containsKey(var)){
                throw new IllegalArgumentException("Every variable should have a domain assigned to it.");
            }
        }
    }

    // 遍历变量，并增加约束
    public void addConstraint(Constraint<V, D> constraint){
        for(V var: variables){
            if(!variables.contains(var)){
                throw new IllegalArgumentException("Variable in constraint not in CSP.");
            }
            constraints.get(var).add(constraint);
        }
    }

    // 所有var 都满足条件
    public boolean consistent(V var, Map<V, D> assignment){
        for(Constraint<V, D> cst: constraints.get(var)){
            if (!cst.satisfied(assignment)){
                return false;
            }
        }
        return true;
    }

    // 回溯搜索：递归的DFS
    public Map<V, D> backtrackingSearch(Map<V, D> assignment){
        // 每个变量都找到满足条件的赋值，则停止
        if(assignment.size() == variables.size()){
            return assignment;
        }
        // 变量并找出一个未被赋值的变量
        V unassigned = variables.stream().filter(v -> !assignment.containsKey(v)).findFirst().get();
        for(D value: domains.get(unassigned)){
            Map<V, D> locAssignment = new HashMap<>(assignment);
            locAssignment.put(unassigned, value);
            // 如果locAssignment可以满足变量要求，且res非空，则刷新 assignment 并进行递归
            if(consistent(unassigned, locAssignment)){
                Map<V, D> res = backtrackingSearch(locAssignment);
                if(res != null){
                    return res;
                }
            }
        }
        return null;
    }

    public Map<V, D> backtrackingSearch(){
        return backtrackingSearch(new HashMap<>());
    }
}
