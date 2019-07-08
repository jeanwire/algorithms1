/* *****************************************************************************
 *  Name: Kathleen
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int n;

    public RandomizedQueue() {

        a = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {

        return this.n == 0;
    }

    public int size() {

        return this.n;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public void enqueue(Item item) {

        if (item == null) throw new java.lang.IllegalArgumentException("Null item");

        if (n == a.length) resize(2 * a.length);
        // inserts item, then increments n
        a[n++] = item;

    }

    public Item dequeue() {

        if (isEmpty()) throw new java.util.NoSuchElementException("Empty queue");

        int index = StdRandom.uniform(0, n);
        Item item = a[index];

        Item[] temp = (Item[]) new Object[a.length];
        System.arraycopy(a, 0, temp, 0, index);
        System.arraycopy(a, index + 1, temp, index, n - index - 1);
        a = temp;

        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);

        return item;
    }

    public Item sample() {

        if (isEmpty()) throw new java.util.NoSuchElementException("Empty queue");

        int index = StdRandom.uniform(0, n);

        return a[index];
    }

    public Iterator<Item> iterator() {

        StdRandom.shuffle(a, 0, n);
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {

        private int i = n - 1;

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return a[i--];
        }
    }

    public static void main(String[] args) {

        RandomizedQueue<Integer> trial = new RandomizedQueue<Integer>();

        for (int i = 0; i < 10; i++) {
            trial.enqueue(i);
        }

        System.out.println("dequeue: " + trial.dequeue());

        for (int number : trial) {
            System.out.println(number);
        }
    }
}

