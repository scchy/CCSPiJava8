package chapter3;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName WordGrid.java
 * @Description
 * @createTime 2023年03月07日 16:27:00
 */
public class WordGrid {
    // 定义网格上一个位置的数据结构
    public static class GridLocation{
        public final int row, column;
        public GridLocation(int row, int column){
            this.row = row;
            this.column = column;
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int res = 1;
            res = prime * res + column;
            res = prime * res + row;
            return res;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            GridLocation other = (GridLocation) obj;
            if (column != other.column) {
                return false;
            }
            return row == other.row;
        }
    }

    private final char ALPHABET_LENGTH = 26;
    private final char FIRST_LETTER = 'A';
    private final int rows, columns;
    private char[][] grid;

    public WordGrid(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        grid = new char[rows][columns];
        Random rd = new Random();
        for(int r=0; r < rows; r++){
            for(int c=0; c < columns; c++){
                char rdLetter = (char) (rd.nextInt(ALPHABET_LENGTH) + FIRST_LETTER);
                grid[r][c] = rdLetter;
            }
        }
    }

    public void mark(String word, List<GridLocation> locs){
        for(int i=0; i<word.length(); i++){
            GridLocation loc = locs.get(i);
            grid[loc.row][loc.column] = word.charAt(i);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(char[] rArray: grid){
            sb.append(rArray);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public List<List<GridLocation>> generateDomain(String word){
        List<List<GridLocation>> domain = new ArrayList<>();
        int len = word.length();
        for(int r=0; r < rows; r++){
            for(int c=0; c < columns; c++){
                if(c + len <= columns){
                    fillRight(domain, r, c, len);
                    if(r + len <= rows){
                        fillDiagonalRight(domain, r, c, len);
                    }
                }
                if(r + len <= rows){
                    fillDown(domain, r, c, len);
                    if(c - len >= 0){
                        fillDiagonalLeft(domain, r, c, len);
                    }
                }
            }
        }
        return domain;
    }

    // 从指定位置 从左到右填充
    private void fillRight(List<List<GridLocation>> domain, int r, int c, int len){
        List<GridLocation> locs = new ArrayList<>();
        for(int col=c; col<c+len; col++){
            locs.add(new GridLocation(r, col));
        }
        domain.add(locs);
    }

    // 从指定位置 从上到下填充
    private void fillDown(List<List<GridLocation>> domain, int r, int c, int len){
        List<GridLocation> locs = new ArrayList<>();
        for(int row=r; row<r+len; row++){
            locs.add(new GridLocation(row, c));
        }
        domain.add(locs);
    }

    private void fillDiagonalRight(List<List<GridLocation>> domain, int row, int column, int length) {
        List<GridLocation> locations = new ArrayList<>();
        int r = row;
        for (int c = column; c < (column + length); c++) {
            locations.add(new GridLocation(r, c));
            r++;
        }
        domain.add(locations);
    }

    private void fillDiagonalLeft(List<List<GridLocation>> domain, int row, int column, int length) {
        List<GridLocation> locations = new ArrayList<>();
        int c = column;
        for (int r = row; r < (row + length); r++) {
            locations.add(new GridLocation(r, c));
            c--;
        }
        domain.add(locations);
    }

}
