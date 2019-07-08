/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;


public class Deque<Item> implements Iterable<Item> {

    private int n;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node previous;
        private Node next;
    }

    public Deque() {

        n = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() {

        return this.n == 0;
    }

    public int size() {

        return this.n;
    }

    public void addFirst(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException("Cannot accept null argument");
        }

        Node oldFirst = this.first;
        Node node = new Node();
        node.item = item;
        node.next = oldFirst;
        if (oldFirst != null) {
            oldFirst.previous = node;
        }
        first = node;

        if (last == null) {
            last = node;
        }

        this.n++;
    }

    public void addLast(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException("Cannot accept null argument");
        }

        Node oldLast = this.last;
        Node node = new Node();
        node.item = item;
        if (oldLast != null) {
            oldLast.next = node;
        }
        node.previous = oldLast;
        last = node;

        if (first == null) {
            first = node;
        }

        this.n++;
        // last.next automatically set to null (?)
    }

    public Item removeFirst() {

        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }

        Item item = first.item;
        if (first.next != null) {
            first = first.next;
        }
        first.previous = null;
        n--;

        if (n == 0) {
            first = null;
            last = null;
        }
        return item;
    }

    public Item removeLast() {

        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }

        Item item = last.item;
        if (last.previous != null) {
            last = last.previous;
        }
        last.next = null;
        n--;

        if (n == 0) {
            first = null;
            last = null;
        }

        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    public static void main(String[] args) {

        Deque<Integer> trial = new Deque<Integer>();

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                trial.addFirst(i);
            }
            else {
                trial.addLast(i);
            }
        }

        System.out.println("Size is " + trial.size());

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                System.out.println(trial.removeLast());
            }
            else {
                System.out.println(trial.removeFirst());
            }
        }

        System.out.println("Size is " + trial.size());

        trial.addLast(7);
        System.out.println("Size is " + trial.size());
        System.out.println(trial.first.item);
        System.out.println(trial.last.item);
    }
}

