import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int n;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (first == null && last == null) {
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            first.prev = null;
            if (oldFirst != null) oldFirst.prev = first;
        }

        n += 1;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (first == null && last == null) {
            last = new Node();
            last.item = item;
            last.prev = null;
            last.next = null;
            first = last;
        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            oldLast.next = last;
            last.prev = oldLast;
            last.next = null;
        }
        n += 1;
    }

    public Item removeFirst() {
        if (first == null) throw new NoSuchElementException();
        Item removed = first.item;
        first = first.next;
        if (first == null)
            last = null;
        else
            first.prev = null;
        n -= 1;
        return removed;
    }

    public Item removeLast() {
        if (last == null) throw new NoSuchElementException();
        Item removed = last.item;
        last = last.prev;
        if (last == null)
            first = null;
        else
            last.next = null;
        n -= 1;
        return removed;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (current == null) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        System.out.println(d.size());
        d.addFirst(4);
        d.addFirst(3);
        d.addLast(5);
        d.addLast(6);
        d.addFirst(2);
        d.removeFirst();
        d.removeFirst();
        d.removeLast();
        d.removeLast();
        d.removeLast();
        System.out.println(d.size());
        d.addFirst(4);
        d.addFirst(3);
        d.addLast(5);
        d.addLast(6);
        d.addFirst(2);
        System.out.println(d.size());
        for (int i : d) {
            System.out.println(i);
        }

    }
}
