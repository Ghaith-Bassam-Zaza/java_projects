

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author ghaith
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] itemArr;
    private int next;

    // construct an empty randomized queue
    public RandomizedQueue() {
        itemArr = (Item[]) new Object[1];
        next = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return next == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return next;

    }

    // add the item
    public void enqueue(Item item) {
        if (item != null) {
            if (next >= itemArr.length) {
                resize(2 * itemArr.length);
            }
            itemArr[next++] = item;

        } else {
            throw new IllegalArgumentException();
        }
    }

    private void resize(int newSize) {
        Item[] newArr = (Item[]) new Object[newSize];
        System.arraycopy(itemArr, 0, newArr, 0, next);
        itemArr = newArr;
    }

    // remove and return a random item
    public Item dequeue() {
        if (next > 0) {
            int i = StdRandom.uniform(next);
            Item item = itemArr[i];
            itemArr[i] = itemArr[--next];
            if (next <= 0.25 * itemArr.length) {
                resize((int) (0.5 * itemArr.length));
            }
            return item;

        } else {
            throw new NoSuchElementException();
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int i = StdRandom.uniform(next);
        return itemArr[i];
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();

    }

    private class ListIterator implements Iterator<Item> {

        private int iterNext = next;

        @Override
        public boolean hasNext() {
            return iterNext > 0;
        }

        @Override
        public void remove() {

            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (iterNext != 0) {
                int i = StdRandom.uniform(iterNext);
                Item item = itemArr[i];
                itemArr[i] = itemArr[--iterNext];
                itemArr[iterNext] = item;
                return item;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> que = new RandomizedQueue<>();
        que.enqueue(22);
        que.enqueue(12);
        que.enqueue(34);
        que.enqueue(56);
        que.enqueue(78);
        que.enqueue(15);
        System.out.println(que.isEmpty());
        System.out.println(que.size());
        for (Integer integer : que) {
            System.out.println(integer);
        }
        System.out.println(que.dequeue());
        System.out.println(que.dequeue());
        System.out.println(que.dequeue());
        System.out.println(que.dequeue());
        System.out.println(que.dequeue());
        System.out.println(que.dequeue());
    }

}

