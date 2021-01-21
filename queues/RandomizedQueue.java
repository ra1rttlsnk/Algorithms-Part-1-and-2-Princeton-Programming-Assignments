import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] container;
    private final int head;
    private int tail;

    public RandomizedQueue() {
        n = 0;
        head = 0;
        tail = 0;
        container = (Item[]) new Object[1];
    }

    private Item[] resize() {
        if (n == container.length) {
            Item[] containerNew = (Item[]) new Object[container.length * 2];
            for (int i = head; i < tail; i++) {
                containerNew[i] = container[i];
            }
            return containerNew;
        }
        else if (n == container.length / 4) {
            Item[] containerNew = (Item[]) new Object[container.length / 2];
            for (int i = head; i < tail; i++) {
                containerNew[i] = container[i];
            }
            return containerNew;
        }
        return (Item[]) new Object[0];
    }


    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (n > 0 && n == container.length)
            container = resize();
        container[tail] = item;
        n += 1;
        tail += 1;
    }

    public Item dequeue() {
        if (n == 0) throw new NoSuchElementException();

        int rand = StdRandom.uniform(head, tail);
        Item item = container[rand];
        container[rand] = container[tail - 1];
        container[tail - 1] = null;
        tail -= 1;
        n -= 1;

        if (n > 0 && n == container.length / 4)
            container = resize();
        return item;
    }

    public Item sample() {
        if (n == 0) throw new NoSuchElementException();
        int rand = StdRandom.uniform(head, tail);
        return container[rand];

    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private Item[] containerCopy;
        private final int h;
        private int t;

        public ArrayIterator() {
            containerCopy = (Item[]) new Object[container.length];
            for (int i = head; i < tail; i++) {
                containerCopy[i] = container[i];
            }
            h = head;
            t = tail;
        }

        public boolean hasNext() {
            return t > 0;
        }

        public Item next() {
            if (t == 0) throw new NoSuchElementException();
            int rand = StdRandom.uniform(h, t);
            Item item = containerCopy[rand];
            containerCopy[rand] = containerCopy[t - 1];
            containerCopy[t - 1] = null;
            t -= 1;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> r = new RandomizedQueue<>();
        r.enqueue("to");
        r.enqueue("be");
        r.enqueue("or");
        r.enqueue("not");
        System.out.println(r.size());
        r.dequeue();
        String a = r.dequeue();

        System.out.println(r.size());
        for (String s : r) {
            System.out.println(s);
        }

        for (int i = 0; i < 10; i++)
            System.out.println(r.sample());
        System.out.println(a);

    }

}
