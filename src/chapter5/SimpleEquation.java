package chapter5;

import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName SimpleEquation.java
 * @Description
 * @createTime 2023年03月14日 13:55:00
 */
public class SimpleEquation extends Chromosome<SimpleEquation>{
    private int x, y;
    private static final int MAX_START = 100;
    public SimpleEquation(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static SimpleEquation randomInstance(){
        Random rd = new Random();
        return new SimpleEquation(rd.nextInt(MAX_START), rd.nextInt(MAX_START));
    }

    @Override
    public double fitness(){
        return 6 * x - x * x + 4 * y - y * y;
    }

    @Override
    public List<SimpleEquation> crossover(SimpleEquation oth){
        SimpleEquation c1 = new SimpleEquation(x, oth.y);
        SimpleEquation c2 = new SimpleEquation(oth.x, y);
        return Arrays.asList(c1, c2);
    }

    @Override
    public void mutate(){
        Random rd = new Random();
        if(rd.nextDouble() > 0.5){
            if(rd.nextDouble() > 0.5){
                x += 1;
            }else{
                x -= 1;
            }
        }else{
            if(rd.nextDouble() > 0.5){
                y += 1;
            }else{
                y -= 1;
            }
        }
    }

    @Override
    public SimpleEquation copy(){
        return new SimpleEquation(x, y);
    }

    @Override
    public String toString(){
        return "X: " + x + " Y: " + y + " Fitness: " + fitness();
    }

    public static void main(String[] args){
        ArrayList<SimpleEquation> initialPopulation = new ArrayList<>();
        final int POPULATION_SIZE = 20;
        final int GENERATIONS = 100;
        final double THRESHOLD = 13.0;
        for(int i = 0; i < POPULATION_SIZE; i++){
            initialPopulation.add(SimpleEquation.randomInstance());
        }
        GeneticAlgorithm<SimpleEquation> ga = new GeneticAlgorithm<>(
                initialPopulation, 0.1, 0.7, GeneticAlgorithm.SelectionType.TOURNAMENT
        );
        SimpleEquation res = ga.run(GENERATIONS, THRESHOLD);
        System.out.println(res);

        System.out.println("Integer.parseInt:" + Integer.parseInt("0110") / 100);
    }
}
