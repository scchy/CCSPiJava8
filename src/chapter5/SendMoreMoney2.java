package chapter5;

import java.util.*;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName SendMoreMoney2.java
 * @Description
 * @createTime 2023年03月15日 11:51:00
 */
public class SendMoreMoney2 extends Chromosome<SendMoreMoney2>{
    private List<Character> letters;
    private Random random;

    public SendMoreMoney2(List<Character> letters) {
        this.letters = letters;
        random = new Random();
    }

    public static SendMoreMoney2 randomInstance() {
        List<Character> letters = Arrays.asList(
                'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y', ' ', ' '
        );
        Collections.shuffle(letters);
        return new SendMoreMoney2(letters);
    }

    // 类似损失函数 1 / mae
    @Override
    public double fitness() {
        int s = letters.indexOf('S');
        int e = letters.indexOf('E');
        int n = letters.indexOf('N');
        int d = letters.indexOf('D');
        int m = letters.indexOf('M');
        int o = letters.indexOf('O');
        int r = letters.indexOf('R');
        int y = letters.indexOf('Y');
        int send = s * 1000 + e * 100 + n * 10 + d;
        int more = m * 1000 + o * 100 + r * 10 + e;
        int money = m * 10000 + o * 1000 + n * 100 + e * 10 + y;
        int diff = Math.abs(money - (send + more));
        return 1.0 / (diff + 1.0);
    }

    @Override
    public List<SendMoreMoney2> crossover(SendMoreMoney2 other){
        SendMoreMoney2 c1 = new SendMoreMoney2(new ArrayList<>(letters));
        SendMoreMoney2 c2 = new SendMoreMoney2(new ArrayList<>(other.letters));
        int idx1 = random.nextInt(letters.size());
        int idx2 = random.nextInt(other.letters.size());
        Character l1 = letters.get(idx1);
        Character l2 = other.letters.get(idx2);

        int idx3 = letters.indexOf(l2);
        int idx4 = other.letters.indexOf(l1);
        // child1 随机位置idx1, 和child2随机抽出的字母，在当前letter位置idx3交换
        Collections.swap(c1.letters, idx1, idx3);
        // child2 随机位置idx2, 和child1随机抽出的字母，在当前letter位置idx4交换
        Collections.swap(c2.letters, idx2, idx4);
        return Arrays.asList(c1, c2);
    }

    @Override
    public void mutate() {
        int idx1 = random.nextInt(letters.size());
        int idx2 = random.nextInt(letters.size());
        Collections.swap(letters, idx1, idx2);
    }

    @Override
    public SendMoreMoney2 copy(){
        return new SendMoreMoney2(new ArrayList<>(letters));
    }

    @Override
    public String toString() {
        int s = letters.indexOf('S');
        int e = letters.indexOf('E');
        int n = letters.indexOf('N');
        int d = letters.indexOf('D');
        int m = letters.indexOf('M');
        int o = letters.indexOf('O');
        int r = letters.indexOf('R');
        int y = letters.indexOf('Y');
        int send = s * 1000 + e * 100 + n * 10 + d;
        int more = m * 1000 + o * 100 + r * 10 + e;
        int money = m * 10000 + o * 1000 + n * 100 + e * 10 + y;
        int difference = Math.abs(money - (send + more));
        return ("[" + letters.toString() + "]: SEND("+ send + ") + MORE(" + more + ") = MONEY(" + money + ") Difference: " + difference);
    }

    public static void main(String[] args){
        ArrayList<SendMoreMoney2> initialPopulation = new ArrayList<>();
        final int POPULATION_SIZE = 1000;
        final int GENERATIONS = 1000;
        final double THRESHOLD = 1.0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            initialPopulation.add(SendMoreMoney2.randomInstance());
        }
        GeneticAlgorithm<SendMoreMoney2> ga = new GeneticAlgorithm<>(
                initialPopulation,
                0.2, 0.7,
                GeneticAlgorithm.SelectionType.ROULETTE
        );
        SendMoreMoney2 res = ga.run(GENERATIONS, THRESHOLD);
        System.out.println(res);
    }

}
