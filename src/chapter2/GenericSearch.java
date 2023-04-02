package chapter2;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName GenericSearch.java
 * @Description
 * @createTime 2023年03月02日 11:11:00
 */
public class GenericSearch {
    public static <T extends Comparable<T>> boolean linearContains(List<T> list, T key){
        for(T item: list){
            if(item.compareTo(key) == 0){
                return true;
            }
        }
        return false;
    }

    public static <T extends Comparable<T>> boolean binaryContains(List<T> list, T key){
        int low = 0 ;
        int high = list.size() - 1;
        while(low < high){
            int mid = (low + high) / 2;
            int cmp = list.get(mid).compareTo(key);
            if (cmp < 0){
                low = mid + 1;
            }else if(cmp > 0){
                high = mid - 1;
            }else{
                return true;
            }
        }
        return false;
    }

    public static class Node<T> implements Comparable<Node<T>> {
        final T state;
        Node<T> parent;
        double cost;
        double heuristic;

        Node(T state, Node<T> parent){
            this.state = state;
            this.parent = parent;
        }

        Node(T state, Node<T> parent, double cost, double heuristic) {
            this.state = state;
            this.parent = parent;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(Node<T> oth){
            Double mine = cost + heuristic;
            Double theirs = oth.cost + oth.heuristic;
            return mine.compareTo(theirs);
        }
    }

    public static <T> Node<T> dfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors){
        Stack<Node<T>> helperSpace = new Stack<>();
        helperSpace.push(new Node(initial, null));
        Set<T> visited = new HashSet<>();
        visited.add(initial);
        while(!helperSpace.isEmpty()){
            Node<T> nowN = helperSpace.pop();
            T nowS = nowN.state;
            if(goalTest.test(nowS)){
                return nowN;
            }
            for(T child: successors.apply(nowS)){
                if (visited.contains(child)){
                    continue;
                }
                visited.add(child);
                helperSpace.push(new Node<>(child, nowN));
            }
        }
        return null;
    }

    public static <T> Node<T> bfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors){
        Queue<Node<T>> helperSpace = new LinkedList<>();
        helperSpace.offer(new Node<>(initial, null));
        Set<T> visited = new HashSet<>();
        visited.add(initial);
        while (!helperSpace.isEmpty()){
            Node<T> nowN = helperSpace.poll();
            T nowS = nowN.state;
            if(goalTest.test(nowS)){
                return nowN;
            }
            for(T child: successors.apply(nowS)){
                if(visited.contains(child)){
                    continue;
                }
                visited.add(child);
                helperSpace.offer(new Node<>(child, nowN));
            }
        }
        return null;
    }

    // 逆序路径
    public static <T> List<T> nodeToPath(Node<T> node){
        List<T> path = new ArrayList<>();
        path.add(node.state);
        while(node.parent != null){
            node = node.parent;
            path.add(0, node.state);
        }
        return path;
    }

    public static <T> Node<T> astar(T initial, Predicate<T> goalTest, Function<T, List<T>> successors, ToDoubleFunction<T> heuristic){
        PriorityQueue<Node<T>> pQueue = new PriorityQueue<>();
        pQueue.offer(new Node<>(initial, null, 0.0, heuristic.applyAsDouble(initial)));
        Map<T, Double> visited = new HashMap<>();
        visited.put(initial, 0.0);
        while(!pQueue.isEmpty()) {
            Node<T> nowN = pQueue.poll();
            T nowS = nowN.state;
            if(goalTest.test(nowS)){
                return nowN;
            }
            for(T child: successors.apply(nowS)){
                double newCost = nowN.cost + 1;
                if(!visited.containsKey(child) || visited.get(child) > newCost){
                    visited.put(child, newCost);
                    pQueue.offer(new Node<>(child, nowN, newCost, heuristic.applyAsDouble(child)));
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(linearContains(Arrays.asList(1, 5, 15, 15, 15, 15, 20), 5)); // true
        System.out.println(binaryContains(Arrays.asList("a", "d", "e", "f", "z"), "f")); // true
        System.out.println(binaryContains(Arrays.asList("john", "mark", "ronald", "sarah"), "sheila")); // false
    }
}
