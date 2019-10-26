public class LinkedListDeque<T> {
    private Node sentinel;
    private int size = 0;

    private class Node{
        T item;
        Node prev = null;
        Node rest = null;
        Node(T it){
            this.item = it;
        }
    }

    public void printDeque(){
        for (int i=0; i != this.size-1; i++){
            T item = this.get(i);
            System.out.print(item+" ");
        }
        System.out.println(this.get(this.size-1));
    }


    public void addFirst(T item){
        sentinel.rest.prev = new Node(item);
        sentinel.rest.prev.rest = sentinel.rest;
        sentinel.rest = sentinel.rest.prev;
        sentinel.rest.prev = sentinel;
        this.size++;
    }

    public void addLast(T item){
        sentinel.prev.rest = new Node(item);
        sentinel.prev.rest.prev = sentinel.prev;
        sentinel.prev = sentinel.prev.rest;
        sentinel.prev.rest = sentinel;
        this.size++;
    }

    public boolean isEmpty(){
        if (this.size == 0){
            return true;
        } else {return false;}
    }

    public T removeFirst(){
        if (this.isEmpty()){return null;}
        T res = sentinel.rest.item;
        sentinel.rest = sentinel.rest.rest;
        sentinel.rest.prev = sentinel;
        this.size--;
        return res;
    }

    public T removeLast(){
        if (this.isEmpty()){return null;}
        T res = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.rest = sentinel;
        this.size--;
        return res;
    }

    public T get(int index){
        if (index >= this.size)
            throw new IllegalArgumentException("Out of Range");

        Node p = sentinel.rest;
        int ind = 0;
        while (ind != index){
            p = p.rest;
            ind++;
        }
        return p.item;
    }

    public T getRecursive(int index){
        if (index >= this.size)
            throw new IllegalArgumentException("Out of Range");

        return getRecursiveHelper(sentinel.rest, index);
    }

    public T getRecursiveHelper(Node n, int index){
        if (index == 0)
            return n.item;
        else
            return getRecursiveHelper(n.rest, --index);
    }


    public int size(){
        return this.size;
    }

    public LinkedListDeque(){
        sentinel = new Node(null);
        sentinel.rest = sentinel;
        sentinel.prev = sentinel;
    }

    public LinkedListDeque(LinkedListDeque<T> other){
        sentinel = new Node(null);
        sentinel.rest = sentinel;
        sentinel.prev = sentinel;
        for (int i=0; i!=other.size(); i++)
            this.addFirst(other.get(i));
    }
}
