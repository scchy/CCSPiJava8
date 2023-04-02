package chapter5;

import java.util.*;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName GeneticAlgorithm.java
 * @Description 遗传算法框架
 * @createTime 2023年03月14日 12:49:00
 */
public class GeneticAlgorithm<C extends Chromosome<C>>{
    public enum SelectionType{
        ROULETTE, TOURNAMENT;
    }

    private ArrayList<C> population;
    // 突变概率
    private double mutationChance;
    // 带有混合基因的概率
    private double crossoverChance;
    private SelectionType selectionType;
    private Random random;

    public GeneticAlgorithm(List<C> initialPopulation,
                            double mutationChance, double crossoverChance, SelectionType selectionType){
        this.population = new ArrayList<>(initialPopulation);
        this.mutationChance = mutationChance;
        this.crossoverChance = crossoverChance;
        this.selectionType = selectionType;
        this.random = new Random();
    }

    /* 使用wheel分布挑选 numPicks样本； 和
    python中下列类似：
        import numpy as np
        from scipy.special import softmax
        np.random.choice(range(10), size=2, p=softmax(np.linspace(0, 1, 10)), replace=False)
     */
    private List<C> pickRoulette(double[] wheel, int numPicks){
        List<C> picks = new ArrayList<>();
        for(int i = 0; i < numPicks; i++){
            double pick = random.nextDouble();
            for(int j = 0; j < wheel.length; j++){
                pick -= wheel[j];
                if(pick <= 0){
                    picks.add(population.get(j));
                    break;
                }
            }
        }
        return picks;
    }

    // 分块(numParticipants)随机抽样 - 取块中TOP n (numPicks)
    private List<C> pickTournament(int numParticipants, int numPicks){
        Collections.shuffle(population);
        List<C> tournament = population.subList(0, numParticipants);
        tournament.sort(Collections.reverseOrder());
        return tournament.subList(0, numPicks);
    }

    private void reproduceAndReplace(){
        ArrayList<C> nextPopulation = new ArrayList<>();
        while(nextPopulation.size() < population.size()){
            // 选取双亲
            List<C> parents;
            if (selectionType == SelectionType.ROULETTE) {
                // 获取总 适应度
                double totalFitness = population.stream()
                        .mapToDouble(C::fitness).sum();
                // 适应度分布
                double[] wheel = population.stream()
                        .mapToDouble(C -> C.fitness() / totalFitness)
                        .toArray();
                // 基于适用度从群体随机抽取2个
                parents = pickRoulette(wheel, 2);
            }else{
                // 随机抽一半，再取适应度最高的2个
                parents = pickTournament(population.size() / 2, 2);
            }

            // 如果可以进行染色体融合
            if(random.nextDouble() < crossoverChance){
                C p1 = parents.get(0);
                C p2 = parents.get(1);
                nextPopulation.addAll(p1.crossover(p2));
            }else{
                nextPopulation.addAll(parents);
            }
        }
        if (nextPopulation.size() > population.size()){
            nextPopulation.remove(0);
        }
        // 保持人口一致的换代
        population = nextPopulation;
    }

    // 发生随机变化
    private void mutate(){
        for(C ind: population){
            if(random.nextDouble() < mutationChance){
                ind.mutate();
            }
        }
    }

    public C run(int maxGenerations, double threshold){
        // 存储最佳染色体
        C best = Collections.max(population).copy();
        for(int g=0; g<maxGenerations; g++){
            // 适应度达到一定值，则停止
            if(best.fitness() >= threshold){
                return best;
            }
            System.out.println("Generation " + g +
                    " Best " + best.fitness() +
                    " Avg " + population.stream()
                    .mapToDouble(C::fitness).average().orElse(0.0));
            // 更新迭代
            reproduceAndReplace();
            // 突变
            mutate();
            C highest = Collections.max(population);
            if(highest.fitness() > best.fitness()){
                best = highest.copy();
            }
        }
        return best;
    }

}
