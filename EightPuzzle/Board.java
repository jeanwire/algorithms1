/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    private final int size;
    private final int[][] tiles;

    public Board(int[][] tiles) {

        this.size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], size);
        }
    }

    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append(this.size + "\n");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }

        return s.toString();

    }

    public int dimension() {

        return this.size;
    }

    public int hamming() {
        int total = 0;

        int[][] correct = new int[size][size];
        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                correct[i][j] = num;
                num++;
            }
        }
        correct[size - 1][size - 1] = 0;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (tiles[i][j] == 0) continue;
                else if (tiles[i][j] != correct[i][j]) total++;
            }
        }
        return total;
    }

    public int manhattan() {
        int total = 0;

        int[][] correct = new int[size][size];
        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                correct[i][j] = num;
                num++;
            }
        }
        correct[size - 1][size - 1] = 0;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (tiles[i][j] == 0) continue;
                else if (tiles[i][j] != correct[i][j]) {
                    int row = (tiles[i][j] - 1) / size;
                    int col = (tiles[i][j] - 1) % size;
                    total += java.lang.Math.abs(row - i);
                    total += java.lang.Math.abs(col - j);
                }
            }
        }

        return total;
    }

    public boolean isGoal() {

        int[][] correct = new int[size][size];
        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                correct[i][j] = num;
                num++;
            }
        }
        correct[size - 1][size - 1] = 0;

        return this.equals(new Board(correct));
    }

    public boolean equals(Object y) {

        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.size) return false;
        if (this.toString().equals(that.toString())) {
            return true;
        }

        return false;
    }

    public Iterable<Board> neighbors() {

        Stack<Board> stack = new Stack<Board>();

        int row = 0;
        int col = 0;
        for (int i = 0; i < size * size; i++) {
            if (tiles[i / size][i % size] == 0) {
                row = i / size;
                col = i % size;
            }
        }

        if (row != 0) {
            int[][] neighbor = new int[size][size];
            for (int i = 0; i < size; i++) {
                neighbor[i] = Arrays.copyOf(tiles[i], size);
            }

            neighbor[row][col] = neighbor[row - 1][col];
            neighbor[row - 1][col] = 0;
            stack.push(new Board(neighbor));
        }

        if (row != size - 1) {
            int[][] neighbor = new int[size][size];
            for (int i = 0; i < size; i++) {
                neighbor[i] = Arrays.copyOf(tiles[i], size);
            }

            neighbor[row][col] = neighbor[row + 1][col];
            neighbor[row + 1][col] = 0;
            stack.push(new Board(neighbor));
        }

        if (col != 0) {
            int[][] neighbor = new int[size][size];
            for (int i = 0; i < size; i++) {
                neighbor[i] = Arrays.copyOf(tiles[i], size);
            }

            neighbor[row][col] = neighbor[row][col - 1];
            neighbor[row][col - 1] = 0;
            stack.push(new Board(neighbor));
        }

        if (col != size - 1) {
            int[][] neighbor = new int[size][size];
            for (int i = 0; i < size; i++) {
                neighbor[i] = Arrays.copyOf(tiles[i], size);
            }

            neighbor[row][col] = neighbor[row][col + 1];
            neighbor[row][col + 1] = 0;
            stack.push(new Board(neighbor));
        }

        return stack;
    }


    public Board twin() {

        int[][] twin = new int[size][size];

        for (int i = 0; i < size; i++) {
            twin[i] = Arrays.copyOf(this.tiles[i], size);
        }

        int row1 = 0;
        int row2 = 0;
        int col1 = 0;
        int col2 = 0;

        // start from the beginning for the first tile to swap
        for (int i = 0; i < size * size; i++) {
            if (this.tiles[i / size][i % size] != 0) {
                row1 = i / size;
                col1 = i % size;
                break;
            }
        }

        // start from the end for the second tile to swap
        for (int i = size * size - 1; i >= 0; i--) {
            if (this.tiles[i / size][i % size] != 0) {
                row2 = i / size;
                col2 = i % size;
                break;
            }
        }

        int val1 = twin[row1][col1];
        twin[row1][col1] = twin[row2][col2];
        twin[row2][col2] = val1;

        return new Board(twin);
    }

    public static void main(String[] args) {

        int[][] board = new int[3][3];
        board[0][0] = 8;
        board[0][1] = 1;
        board[0][2] = 3;
        board[1][0] = 4;
        board[1][1] = 0;
        board[1][2] = 2;
        board[2][0] = 7;
        board[2][1] = 6;
        board[2][2] = 5;
        Board trial = new Board(board);
        System.out.println(trial.dimension());
        Board trial2 = new Board(board);
        System.out.println(trial.equals(trial2));
        System.out.println(trial.toString());
        System.out.println(trial.hamming());
        System.out.println(trial.manhattan());
        System.out.println(trial.twin().toString());
        Iterable<Board> stack = trial.neighbors();
        for (Board neighbor : stack) {
            System.out.println(neighbor.toString());
        }
    }
}
