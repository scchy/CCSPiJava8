package chapter9;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName TSP.java
 * @Description
 * @createTime 2023年04月02日 19:00:00
 */
public class TSP {
    private final Map<String, Map<String, Integer>> distances;

    public TSP(Map<String, Map<String, Integer>> d){
        this.distances = d;
    }

    public static <T> void swap(T[] array, int f, int s) {
        T tmp = array[f];
        array[f] = array[s];
        array[s] = tmp;
    }

    private static <T> void allPermutationsHelper(T[] p, List<T[]> permutations, int n){
        if(n<=0){
            permutations.add(p);
            return;
        }
        T[] tmp = Arrays.copyOf(p, p.length);
        for(int i = 0; i < n; i++){
            swap(tmp, i, n-1);
            allPermutationsHelper(tmp, permutations, n-1);
            swap(tmp, i, n-1);
        }
    }

    private static <T> List<T[]> permutations(T[] original){
        List<T[]> p = new ArrayList<>();
        allPermutationsHelper(original, p, original.length);
        return p;
    }

    public int pathDistance(String[] path){
        String last = path[0];
        int dis = 0;
        for(String next: Arrays.copyOfRange(path, 1, path.length)){
            dis += distances.get(last).get(next);
            last = next;
        }
        return dis;
    }

    public String[] findShortestPath(){
        List<String> ct = new ArrayList<>(distances.keySet());
        String[] cities = ct.toArray(new String[0]);
        System.out.println("cities="+ Arrays.asList(cities));

        List<String[]> paths = permutations(cities);
        String[] shortestPath = null;
        int minDistance = Integer.MAX_VALUE;
        for(String[] p: paths){
            int dis = pathDistance(p);
            dis += distances.get(p[p.length-1]).get(p[0]);
            if(dis < minDistance){
                minDistance = dis;
                shortestPath = p;
            }
        }

        shortestPath = Arrays.copyOf(shortestPath, shortestPath.length+1);
        shortestPath[shortestPath.length-1] = shortestPath[0];
        return shortestPath;
    }

    public static void main(String[] args){
        Map<String, Map<String, Integer>> vtDistances = new HashMap<String,  Map<String, Integer>>() {{
            put("Rutland", new HashMap<String, Integer>(){
                {
                    put("Burlington", 67);
                    put("White River Junction", 46);
                    put("Bennington", 55);
                    put("Brattleboro", 75);
                }});
            put("Burlington", new HashMap<String, Integer>(){
                {
                    put("Rutland", 67);
                    put("White River Junction", 91);
                    put("Bennington", 122);
                    put("Brattleboro", 153);
                }});
            put("White River Junction", new HashMap<String, Integer>(){
                {
                    put("Rutland", 46);
                    put("Burlington", 91);
                    put("Bennington", 98);
                    put("Brattleboro", 65);
                }});
            put("Bennington", new HashMap<String, Integer>(){
                {
                    put("Rutland", 55);
                    put("Burlington", 122);
                    put("White River Junction", 98);
                    put("Brattleboro", 40);
                }});
            put("Brattleboro", new HashMap<String, Integer>(){
                {
                    put("Rutland", 75);
                    put("Burlington", 153);
                    put("White River Junction", 65);
                    put("Bennington", 40);
                }});
        }};


        TSP tsp = new TSP(vtDistances);
        String[] shortestPath = tsp.findShortestPath();
        System.out.println(shortestPath.toString());
        int distance = tsp.pathDistance(shortestPath);
        System.out.println("The shortest path is " + Arrays.toString(shortestPath) + " in " +
                distance + " miles.");
    }
}
