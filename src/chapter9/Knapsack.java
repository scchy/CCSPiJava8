package chapter9;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Knapsack.java
 * @Description
 * @createTime 2023年04月02日 18:45:00
 */
public final class Knapsack {
    public static final class Item {
        public final String name;
        public final int weight;
        public final double value;

        public Item(String name, int weight, double value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }
    }

    public static List<Item> knapsack(List<Item> items, int maxCap) {
        double[][] table = new double[items.size() + 1][maxCap + 1];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            for (int cap = 1; cap <= maxCap; cap++) {
                double prevItemValue = table[i][cap];
                if (cap > item.weight) {
                    double freeWeightForItem = table[i][cap - item.weight];
                    table[i + 1][cap] = Math.max(freeWeightForItem + item.value, prevItemValue);
                } else {
                    table[i + 1][cap] = prevItemValue;
                }
            }
        }
        List<Item> solution = new ArrayList<>();
        int capCity = maxCap;
        for (int i = items.size(); i > 0; i--) {
            if (table[i - 1][capCity] != table[i][capCity]) {
                solution.add(items.get(i - 1));
                capCity -= items.get(i - 1).weight;
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("television", 50, 500));
        items.add(new Item("candlesticks", 2, 300));
        items.add(new Item("stereo", 35, 400));
        items.add(new Item("laptop", 3, 1000));
        items.add(new Item("food", 15, 50));
        items.add(new Item("clothing", 20, 800));
        items.add(new Item("jewelry", 1, 4000));
        items.add(new Item("books", 100, 300));
        items.add(new Item("printer", 18, 30));
        items.add(new Item("refrigerator", 200, 700));
        items.add(new Item("painting", 10, 1000));
        List<Item> toSteal = knapsack(items, 75);
        System.out.println("The best items for the thief to steal are:");
        System.out.printf("%-15.15s %-15.15s %-15.15s%n", "Name", "Weight", "Value");
        for (Item item : toSteal) {
            System.out.printf("%-15.15s %-15.15s %-15.15s%n", item.name, item.weight, item.value);
        }
    }
}
