package chapter2;


import chapter2.GenericSearch.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Maze.java
 * @Description
 * @createTime 2023年03月02日 20:55:00
 */
public class Maze {
    public enum Cell {
        EMPTY(" "),
        BLOCKED("X"),
        START("S"),
        GOAL("G"),
        PATH("*");

        private final String code;

        private Cell(String c){
            code = c;
        }

        @Override
        public String toString(){
            return code;
        }
    }

    public static class MazeLocation{
        public final int row;
        public final int column;

        public MazeLocation(int r, int c){
            this.row = r;
            this.column = c;
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int  res = 1;
            res = prime * res + column;
            res = prime * res + row;
            return res;
        }

        @Override
        public boolean equals(Object obj){
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            MazeLocation other = (MazeLocation) obj;
            if (column != other.column) {
                return false;
            }
            return row == other.row;
        }
    }

    private final int rows, columns;
    private final MazeLocation start, goal;
    private Cell[][] grid;

    public Maze(int rows, int columns, MazeLocation start, MazeLocation goal, double sparseness){
        this.rows = rows;
        this.columns = columns;
        this.start = start;
        this.goal = goal;
        // 填充网格
        grid = new Cell[rows][columns];
        for(Cell[] row: grid){
            Arrays.fill(row, Cell.EMPTY);
        }
        // blocked 填充
        randomlyFill(sparseness);
        // 填充开始 和 目标
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public Maze(){
        this(10, 10, new MazeLocation(0, 0), new MazeLocation(9, 9), 0.2);
    }

    private void randomlyFill(double sparseness){
        for(int r=0; r<rows; r++){
            for(int c=0; c<columns; c++){
                if (Math.random() < sparseness){
                    grid[r][c] = Cell.BLOCKED;
                }
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for( Cell[] r: grid){
            for( Cell cell: r){
                sb.append(cell);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public boolean goalTest(MazeLocation ml){
        return goal.equals(ml);
    }

    // 搜索周围空间
    public List<MazeLocation> successors(MazeLocation ml){
        List<MazeLocation> locs = new ArrayList<>();
        // 右边
        if(ml.row + 1 < rows && grid[ml.row+1][ml.column] != Cell.BLOCKED){
            locs.add(new MazeLocation(ml.row+1, ml.column));
        }
        // 左边
        if(ml.row - 1 >= 0 && grid[ml.row-1][ml.column] != Cell.BLOCKED){
            locs.add(new MazeLocation(ml.row-1, ml.column));
        }
        // 下边
        if(ml.column + 1 < columns && grid[ml.row][ml.column+1] != Cell.BLOCKED){
            locs.add(new MazeLocation(ml.row, ml.column+1));
        }
        // 上边
        if(ml.column - 1 >= 0 && grid[ml.row][ml.column-1] != Cell.BLOCKED){
            locs.add(new MazeLocation(ml.row, ml.column-1));
        }
        return locs;
    }

    public void mark(List<MazeLocation> path){
        for(MazeLocation m: path){
            grid[m.row][m.column] = Cell.PATH;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public void clear(List<MazeLocation> path) {
        for (MazeLocation ml : path) {
            grid[ml.row][ml.column] = Cell.EMPTY;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public double euclideanDistance(MazeLocation ml){
        int x = ml.column - goal.column;
        int y = ml.row - goal.row;
        return Math.sqrt((x * x) + (y * y));
    }

    public double manhattanDistance(MazeLocation ml) {
        int x = Math.abs(ml.column - goal.column);
        int y = Math.abs(ml.row - goal.row);
        return (x + y);
    }

    public static void main(String[] args){
        Maze m = new Maze();
        System.out.println(m);

        Node<MazeLocation> s1 = GenericSearch.dfs(m.start, m::goalTest, m::successors);
        if (s1 == null) {
            System.out.println("No solution found using depth-first search!");
        } else {
            List<MazeLocation> path1 = GenericSearch.nodeToPath(s1);
            m.mark(path1);
            System.out.println(m);
            m.clear(path1);
        }

        Node<MazeLocation> s2 = GenericSearch.bfs(m.start, m::goalTest, m::successors);
        if (s2 == null) {
            System.out.println("No solution found using breadth-first search!");
        } else {
            List<MazeLocation> path2 = GenericSearch.nodeToPath(s2);
            m.mark(path2);
            System.out.println(m);
            m.clear(path2);
        }

        Node<MazeLocation> s3 = GenericSearch.astar(m.start, m::goalTest, m::successors, m::manhattanDistance);
        if (s3 == null) {
            System.out.println("No solution found using A*!");
        } else {
            List<MazeLocation> path3 = GenericSearch.nodeToPath(s3);
            m.mark(path3);
            System.out.println(m);
            m.clear(path3);
        }
    }
}
