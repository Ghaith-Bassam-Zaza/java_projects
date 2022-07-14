

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author ghaith
 */
public class Board {

    private static enum direction {
        UP, DOWN, RIGHT, LEFT
    }
    private int blankRow = 0;
    private int blankCol = 0;
    private final int[][] tiles;
    private int ham = 0;
    private int man = 0;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        n = tiles.length;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) {
                    blankCol = col;
                    blankRow = row;
                }else{
                    int pn = row * n + col + 1;
                    
                    if (pn != tiles[row][col]) {
                        ham++;
                        int gapY = Math.abs(row - (tiles[row][col] - 1) / n);
                        int gapX = Math.abs(col - (tiles[row][col] - 1) % n);
                        man += gapY + gapX;
                    }
                }
            }
        }
    }

    // string representation of this board
    @Override
    public String toString() {
        
        String s = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                
                s += String.format("%2d" , tiles[i][j]) + " ";
            }
            s += "\n";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return ham == 0;
    }

    // does this board equal y?
    /**
     *
     * @param y
     * @return
     */
    @Override
    public boolean equals(Object y) {
        if(y != null){
            return this.toString().equals(y.toString());
        }else return false;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Neighbors();
    }

    private class Neighbors implements Iterable<Board> {

        
        private final int blankX = blankCol;
        private final int blankY = blankRow;
        private final Board[] neighbors;
        private int i;
        public Neighbors() {
            
            
            if (blankCol == 0) {
                if (blankRow == 0) {
                    i = 2;
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    neighbors = new Board[i];
                    move(direction.RIGHT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.DOWN,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                    
                    
                } else if (blankRow == n - 1) {
                    i = 2;
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    neighbors = new Board[i];
                    move(direction.RIGHT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.UP,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                } else {
                    i = 3;
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    neighbors = new Board[i];
                    move(direction.RIGHT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.DOWN,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                    int[][] neighborArr2 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr2[j], 0, n);
                    }
                    move(direction.UP,neighborArr2);
                    neighbors[2] = new Board(neighborArr2);
                }
            } else if (blankCol == n - 1) {
                if (blankRow == 0) {
                    i = 2;
                    neighbors = new Board[i];
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    move(direction.LEFT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.DOWN,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                } else if (blankRow == n - 1) {
                    i = 2;
                    neighbors = new Board[i];
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    move(direction.LEFT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.UP,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                } else {
                    i = 3;
                    neighbors = new Board[i];
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    move(direction.LEFT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.DOWN,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                    int[][] neighborArr2 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr2[j], 0, n);
                    }
                    move(direction.UP,neighborArr2);
                    neighbors[2] = new Board(neighborArr2);
                }
            } else {
                if (blankRow == 0) {
                    i = 3;
                    neighbors = new Board[i];
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    move(direction.LEFT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.DOWN,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                    int[][] neighborArr2 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr2[j], 0, n);
                    }
                    move(direction.RIGHT,neighborArr2);
                    neighbors[2] = new Board(neighborArr2);
                } else if (blankRow == n - 1) {
                    i = 3;
                    neighbors = new Board[i];
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    move(direction.LEFT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.UP,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                    int[][] neighborArr2 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr2[j], 0, n);
                    }
                    move(direction.RIGHT,neighborArr2);
                    neighbors[2] = new Board(neighborArr2);
                } else {
                    i = 4;
                    neighbors = new Board[i];
                    int[][] neighborArr0 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr0[j], 0, n);
                    }
                    move(direction.LEFT,neighborArr0);
                    neighbors[0] = new Board(neighborArr0);
                    int[][] neighborArr1 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr1[j], 0, n);
                    }
                    move(direction.DOWN,neighborArr1);
                    neighbors[1] = new Board(neighborArr1);
                    int[][] neighborArr2 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr2[j], 0, n);
                    }
                    move(direction.RIGHT,neighborArr2);
                    neighbors[2] = new Board(neighborArr2);
                    int[][] neighborArr3 = new int[n][n];
                    for (int j = 0; j < n; j++) {
                        System.arraycopy(tiles[j], 0, neighborArr3[j], 0, n);
                    }
                    move(direction.UP,neighborArr3);
                    neighbors[3] = new Board(neighborArr3);
                }
            }
        }

        private void move(direction d,int[][] neighborArr) {
           
            switch (d) {
                case UP:
                    neighborArr[blankY][blankX]=neighborArr[blankY - 1][blankX];
                    neighborArr [blankY - 1][blankX]= 0;
                    
                    break;
                case DOWN:
                    neighborArr[blankY][blankX]=neighborArr[blankY + 1][blankX];
                    neighborArr [blankY + 1][blankX]= 0;
                    
                    break;
                case LEFT:
                    neighborArr[blankY][blankX]=neighborArr[blankY][blankX - 1];
                    neighborArr [blankY][blankX - 1]= 0;
                   
                    break;
                case RIGHT:
                    neighborArr[blankY][blankX]=neighborArr[blankY][blankX + 1];
                    neighborArr [blankY][blankX + 1]= 0;
                    
                    break;
                default:
                    break;
            }
            
        }

        @Override
        public Iterator<Board> iterator() {
            return new iter();
        }

        private class iter implements Iterator<Board> {
            
            @Override
            public boolean hasNext() {
                return i > 0;
            }

            @Override
            public Board next() {
                return neighbors[--i];
            }

        }

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, twinArr[i], 0, n);
        }
            if(blankRow < n - 1){
                int temp = twinArr[blankRow + 1][blankCol];
                if(blankCol < n - 1){    
                    twinArr[blankRow + 1][blankCol] = twinArr[blankRow][blankCol + 1];
                    twinArr[blankRow][blankCol + 1] = temp;
                }else{
                    twinArr[blankRow + 1][blankCol] = twinArr[blankRow][blankCol - 1];
                    twinArr[blankRow][blankCol - 1] = temp;
                }
                           
                    
            }else{
                int temp = twinArr[blankRow - 1][blankCol];
                if(blankCol < n - 1){    
                    twinArr[blankRow - 1][blankCol] = twinArr[blankRow][blankCol + 1];
                    twinArr[blankRow][blankCol + 1] = temp;  
                }else{
                    twinArr[blankRow - 1][blankCol] = twinArr[blankRow][blankCol - 1];
                    twinArr[blankRow][blankCol - 1] = temp;  
                }
                      
                }
        return new Board(twinArr);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        
    }

}

