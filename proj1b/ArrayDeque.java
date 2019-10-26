public class ArrayDeque<item> implements Deque<item> {
    private int size;
    private int nextFirst;
    private int nextLast;
    private item[] items;
    private static final int INIT_CAPACITY = 8;
    private static final int RESIZE_FACTOR = 2;
    private static final double MIN_USAGE = 0.25;


    public void resize(int capacity){
        item[] newArr = (item[]) new Object[capacity];
        System.arraycopy(items, 0, newArr, 0, size);
        items = newArr;
        nextFirst = indexMinus(0);
        nextLast = size;
    }


    public int indexPlus(int index){
        return (++index) % items.length;
    }


    public int indexMinus(int index){
        return (--index + items.length) % items.length;
    }

    @Override
    public void addFirst(item item){
        if (this.size == items.length)
            this.resize(this.size * RESIZE_FACTOR);

        items[nextFirst] = item;
        nextFirst = indexMinus(nextFirst);
        size++;
    }

    @Override
    public void addLast(item item){
        if (size == items.length)
            resize(size * RESIZE_FACTOR);

        items[nextLast] = item;
        nextLast = indexPlus(nextLast);
        size++;
    }

    @Override
    public boolean isEmpty(){
        if (this.size == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size(){
        return this.size;
    }

    @Override
    public void printDeque(){
        for (int i=indexPlus(nextFirst); i!=indexMinus(nextLast); i++)
            System.out.print(items[i]+" ");
        System.out.println(items[indexMinus(nextLast)]);
    }

    @Override
    public item removeFirst(){
        if (size == 0)
            return null;

        int p = indexPlus(nextFirst);
        item value = items[p];
        items[p] = null;
        size--;
        if (size < items.length*MIN_USAGE){
            resize(items.length/RESIZE_FACTOR);
        }
        return value;
    }

    @Override
    public item removeLast(){
        if (size == 0)
            return null;

        int p = indexMinus(nextLast);
        item value = items[p];
        items[p] = null;
        size--;
        if (size < items.length*MIN_USAGE){
            resize(items.length/RESIZE_FACTOR);
        }
        return value;
    }

    @Override
    public item get(int index){
        return items[index];
    }

    public ArrayDeque(){
        this.items = (item[]) new Object[INIT_CAPACITY];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    public ArrayDeque(ArrayDeque<item> other){
        this.items = (item[]) new Object[other.items.length];
        this.size = other.size;
        this.nextLast = other.nextLast;
        this.nextFirst = other.nextFirst;
        System.arraycopy(other.items, 0, this.items, 0, other.items.length);
    }

}