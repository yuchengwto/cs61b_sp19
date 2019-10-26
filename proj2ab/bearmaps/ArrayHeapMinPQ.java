package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> items;
    private HashMap<T, Integer> indexMap;

    class PriorityNode implements Comparable {
        T item;
        double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() { return item; }
        double getPriority() { return priority; }
        void setPriority(double priority) { this.priority = priority; }

        @Override
        public int compareTo(Object o) {
            return Double.compare(this.getPriority(), ((PriorityNode) o).getPriority());
        }
    }

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        indexMap = new HashMap<>();
        items.add(null);
        indexMap.put(null, 0);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) throw new IllegalArgumentException();
        items.add(new PriorityNode(item, priority));
        indexMap.put(item, size());
        swim(size());
    }

    private void swap(int index1, int index2) {
        if (index1 == index2) return;
        PriorityNode temp1 = items.get(index1);
        PriorityNode temp2 = items.get(index2);
        items.set(index1, temp2);
        items.set(index2, temp1);

        T item1 = temp1.getItem();
        T item2 = temp2.getItem();
        indexMap.replace(item1, index2);
        indexMap.replace(item2, index1);
    }

    private void swim(int index) {
        if (index == 1) return;
        int cmpPC = Double.compare(items.get(parent(index)).getPriority(), items.get(index).getPriority());
        if (cmpPC > 0) {
            swap(index, parent(index));
            swim(parent(index));
        }
    }

    private void sink(int index) {
        if (leftChild(index) > size()) return;
        int cmpPC;

        if (rightChild(index) > size()) {
            cmpPC = items.get(index).compareTo(items.get(leftChild(index)));
            if (cmpPC > 0) {
                swap(index, leftChild(index));
                sink(leftChild(index));
            }
            return;
        }

        int cmpChild = items.get(leftChild(index)).compareTo(items.get(rightChild(index)));
        if (cmpChild < 0 || (cmpChild==0 && Math.random() < 0.5)) {
            cmpPC = items.get(index).compareTo(items.get(leftChild(index)));
            if (cmpPC > 0) {
                swap(index, leftChild(index));
                sink(leftChild(index));
            }
        } else {
            cmpPC = items.get(index).compareTo(items.get(rightChild(index)));
            if (cmpPC > 0) {
                swap(index, rightChild(index));
                sink(rightChild(index));
            }
        }
    }

    private int parent(int index) { return index / 2; }
    private int leftChild(int index) { return index*2; }
    private int rightChild(int index) { return index*2 + 1; }

    @Override
    public boolean contains(T item) {
        return indexMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size()==0) { throw new NoSuchElementException(); }
        return items.get(1).getItem();
    }

    @Override
    public T removeSmallest() {
        if (size()==0) { throw new NoSuchElementException(); }
        swap(1, size());
        T smallestOne = items.remove(size()).getItem();
        indexMap.remove(smallestOne);
        sink(1);
        return smallestOne;
    }

    @Override
    public int size() {
        return items.size() - 1;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) throw new NoSuchElementException();
        int indexTarget = indexMap.get(item);
        swap(indexTarget, size());
        items.remove(size());
        indexMap.remove(item);
        sink(indexTarget);
        add(item, priority);
    }
}
