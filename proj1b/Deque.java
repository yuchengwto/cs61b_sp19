public interface Deque<item> {

    void addFirst(item item);
    void addLast(item item);
    boolean isEmpty();
    int size();
    void printDeque();
    item removeFirst();
    item removeLast();
    item get(int index);


}
