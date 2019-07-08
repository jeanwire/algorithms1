/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> data = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String val = StdIn.readString();
            data.enqueue(val);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(data.dequeue());
        }
    }
}
