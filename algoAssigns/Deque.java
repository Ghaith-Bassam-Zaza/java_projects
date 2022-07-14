

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author ghaith
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node first;
    private Node last;

    private class Node {

        Item item;
        Node next;
        Node prev;

        public Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item != null) {
            if (size == 0) {
                first = new Node(item, null, null);
                last = first;
            } else {
                first.prev = new Node(item, first, null);
                first = first.prev;
            }
            size++;

        } else {
            throw new IllegalArgumentException();
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item != null) {
            if (size == 0) {
                last = new Node(item, null, null);
                first = last;
            } else {
                last.next = new Node(item, null, last);
                last = last.next;
            }
            size++;

        } else {
            throw new IllegalArgumentException();
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size > 0) {
            size--;
            Item oldFirst = first.item;
            first = first.next;
            if (size > 0) {
                first.prev = null;
            }
            return oldFirst;
        } else {
            throw new NoSuchElementException();
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size > 0) {
            size--;
            Item oldLast = last.item;
            last = last.prev;
            if (size > 0) {
                last.next = null;
            }
            return oldLast;
        } else {
            throw new NoSuchElementException();
        }
    }

    // return an iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator() {

        return new ListIterator();

    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;
        private int iterSize = size;

        @Override
        public boolean hasNext() {
            return iterSize != 0;
        }

        @Override
        public void remove() {

            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (iterSize != 0) {
                iterSize--;
                Item item = current.item;
                current = current.next;
                return item;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        System.out.println(deque.isEmpty());
        deque.addFirst(4);
        deque.addLast(22);
        deque.addFirst(211);
        deque.addLast(12);
        deque.addFirst(51);
        deque.addLast(62);
        for (Integer integer : deque) {
            System.out.println(integer);
        }
        System.out.println(deque.isEmpty());
        System.out.println(deque.size());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
    }

}

