package chapter5;

import java.util.List;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Chromosome.java
 * @Description
 * @createTime 2023年03月14日 12:12:00
 */
public abstract class Chromosome<T extends Chromosome<T>> implements Comparable<T> {
    // T extends Chromosome<T> 泛类型与Chromosome绑定 这意味着填入的T类型变量必须是Chrome的子类
    // 确定自己适应度
    public abstract double fitness();
    // 将自己与另一个染色体融合
    public abstract List<T> crossover(T other);
    // 突变-发生随机变化
    public abstract void mutate();
    // 自我复制
    public abstract T copy();

    // 与其他染色体比较
    @Override
    public int compareTo(T other){
        return Double.compare(this.fitness(), other.fitness());
    }
}

