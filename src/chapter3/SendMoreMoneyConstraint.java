package chapter3;

import javax.security.auth.callback.CallbackHandler;
import java.util.*;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName SendMoreMoneyConstraint.java
 * @Description 找到字母代表的数字, 每个字母都代表一个0~9 的数字。同一个数字只会用一个字母来表示
 * @createTime 2023年03月08日 12:36:00
 */
public class SendMoreMoneyConstraint extends Constraint<Character, Integer> {
    private List<Character> letters;

    public SendMoreMoneyConstraint(List<Character> letters) {
        super(letters);
        this.letters = letters;
    }

    @Override
    public boolean satisfied(Map<Character, Integer> assignment){
        // 存在重复则非解
        if((new HashSet<>(assignment.values())).size() < assignment.size()){
            return false;
        }
        // 检查是否正确增加
        if(assignment.size() == letters.size()){
            int s = assignment.get('S');
            int e = assignment.get('E');
            int n = assignment.get('N');
            int d = assignment.get('D');
            int m = assignment.get('M');
            int o = assignment.get('O');
            int r = assignment.get('R');
            int y = assignment.get('Y');
            int send = s * 1000 + e * 100 + n * 10 + d;
            int more = m * 1000 + o * 100 + r * 10 + e;
            int money = m * 10000 + o * 1000 + n * 100 + e * 10 + y;
            return send + more == money;
        }
        return true;
    }
    public static void main(String[] args) {
        List<Character> letters = Arrays.asList('S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y');
        Map<Character, List<Integer>> possibleDigits = new HashMap<>();
        for (Character letter : letters) {
            possibleDigits.put(letter,  Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        }
        // so we don't get answers starting with a 0
        possibleDigits.replace('M', Collections.singletonList(1));
        CSP<Character, Integer> csp = new CSP<>(letters, possibleDigits);
        csp.addConstraint(new SendMoreMoneyConstraint(letters));
        Map<Character, Integer> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found!");
        } else {
            System.out.println(solution);
        }
    }
}
