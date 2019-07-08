/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private Board input;
    private Board correctBoard;
    private Node solutionNode;

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int numMoves;
        private final int manhattan;
        private final int priority;
        private final Node previous;

        public Node(Board b, int p, Node prev, int moves, int manScore) {
            this.board = b;
            this.priority = p;
            this.previous = prev;
            this.numMoves = moves;
            this.manhattan = manScore;
        }

        public int compareTo(Node other) {
            return this.priority > other.priority ? 1 :
                   this.priority < other.priority ? -1 : 0;
        }

        public Comparator<Node> nodeOrder() {
            return new NodeOrder();
        }

        private class NodeOrder implements Comparator<Node> {
            public int compare(Node n1, Node n2) {
                if (n1.priority > n2.priority) return 1;
                else if (n1.priority < n2.priority) return -1;

                return 0;
            }
        }

    }


    public Solver(Board initial) {

        if (initial == null) {
            throw new java.lang.IllegalArgumentException("Null argument");
        }

        input = initial;
        MinPQ<Node> queue = new MinPQ<Node>();
        Node first = new Node(initial, initial.manhattan(), null, 0, initial.manhattan());
        queue.insert(first);

        int size = initial.dimension();

        int[][] correct = new int[size][size];
        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                correct[i][j] = num;
                num++;
            }
        }
        correct[size - 1][size - 1] = 0;
        correctBoard = new Board(correct);

        Node parent = queue.delMin();
        if (isSolvable()) {
            while (!parent.board.toString().equals(correctBoard.toString())) {
                Iterable<Board> neighbors = parent.board.neighbors();
                for (Board neighbor : neighbors) {
                    //  critical optimization
                    if (parent.previous == null || !neighbor.toString()
                                                            .equals(parent.previous.board
                                                                            .toString())) {
                        int manhattan = neighbor.manhattan();
                        int moves = parent.numMoves + 1;
                        queue.insert(
                                new Node(neighbor, manhattan + moves, parent, moves, manhattan));
                    }
                }
                parent = queue.delMin();
            }
        }

        this.solutionNode = parent;
    }

    public boolean isSolvable() {
        MinPQ<Node> thisQueue = new MinPQ<Node>();
        Node first = new Node(input, input.manhattan(), null, 0, input.manhattan());
        thisQueue.insert(first);
        MinPQ<Node> twinQueue = new MinPQ<Node>();
        Board twin = this.input.twin();
        Node twinFirst = new Node(twin, twin.manhattan(), null, 0, twin.manhattan());
        twinQueue.insert(twinFirst);

        while (true) {
            // this board
            Node thisParent = thisQueue.delMin();
            if (thisParent.board.toString().equals(correctBoard.toString())) {
                return true;
            }
            Iterable<Board> neighbors = thisParent.board.neighbors();
            for (Board neighbor : neighbors) {
                if (thisParent.previous == null || !neighbor.toString()
                                                            .equals(thisParent.previous.board
                                                                            .toString())) {
                    int manhattan = neighbor.manhattan();
                    int moves = thisParent.numMoves + 1;
                    thisQueue.insert(new Node(neighbor, manhattan + moves, thisParent, moves,
                                              manhattan));
                }
            }

            // twin board
            Node twinParent = twinQueue.delMin();
            if (twinParent.board.toString().equals(correctBoard.toString())) {
                return false;
            }
            Iterable<Board> twinNeighbors = twinParent.board.neighbors();
            for (Board neighbor : twinNeighbors) {
                if (twinParent.previous == null || !neighbor.toString()
                                                            .equals(twinParent.previous.board
                                                                            .toString())) {
                    int manhattan = neighbor.manhattan();
                    int moves = twinParent.numMoves + 1;
                    twinQueue.insert(new Node(neighbor, manhattan + moves, twinParent, moves,
                                              manhattan));
                }
            }
        }
    }

    public int moves() {

        return solutionNode.numMoves;
    }

    public Iterable<Board> solution() {

        Stack<Board> stack = new Stack<Board>();

        Node node = this.solutionNode;

        while (node.previous != null) {
            stack.push(node.board);
            node = node.previous;
        }

        return stack;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
