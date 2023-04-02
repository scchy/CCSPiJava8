package chapter3;

import javafx.concurrent.WorkerStateEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName WordSearchConstraint.java
 * @Description
 * @createTime 2023年03月07日 17:06:00
 */
public class WordSearchConstraint extends Constraint<String, List<WordGrid.GridLocation>> {
    public WordSearchConstraint(List<String> words){
        super(words);
    }

    @Override
    public boolean satisfied(Map<String, List<WordGrid.GridLocation>> assigment){
        List<WordGrid.GridLocation> allLocs = assigment.values().stream()
                .flatMap(Collection::stream).collect(Collectors.toList());
        Set<WordGrid.GridLocation> allLocsSet = new HashSet<>(allLocs);
        return allLocs.size() == allLocsSet.size();
    }

    public static void main(String[] args){
        WordGrid grid = new WordGrid(9, 9);
        List<String> words = Arrays.asList("MATTHEW", "JOE", "MARY", "SARAH", "SALLY");
        Map<String, List<List<WordGrid.GridLocation>>> domains = new HashMap<>();
        for(String  w: words){
            domains.put(w, grid.generateDomain(w));
        }
        CSP<String, List<WordGrid.GridLocation>> csp = new CSP<>(words, domains);
        csp.addConstraint(new WordSearchConstraint(words));
        Map<String, List<WordGrid.GridLocation>> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            Random random = new Random();
            for (Map.Entry<String, List<WordGrid.GridLocation>> item : solution.entrySet()) {
                String word = item.getKey();
                List<WordGrid.GridLocation> locations = item.getValue();
                // random reverse half the time
                if (random.nextBoolean()) {
                    Collections.reverse(locations);
                }
                grid.mark(word, locations);
            }
            System.out.println(grid);
        }
    }
}
