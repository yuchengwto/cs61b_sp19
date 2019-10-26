import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    private class Node {
        K key;
        V val;
        Node leftchild;
        Node rightchild;

        Node(K k, V v) {
            key = k;
            val = v;
            leftchild = null;
            rightchild = null;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return search(root, key) != null;
    }

    private Node search(Node r, K k) {
        if (r == null) {
            return null;
        }
        if (k.equals(r.key)) {
            return r;
        } else if (k.compareTo(r.key) < 0) {
            return search(r.leftchild, k);
        } else {
            return search(r.rightchild, k);
        }
    }

    private Node insert(Node r, K k, V v) {
        if (r == null) {
            return new Node(k, v);
        }
        if (k.compareTo(r.key) < 0) {
            r.leftchild = insert(r.leftchild, k, v);
        } else {
            r.rightchild = insert(r.rightchild, k, v);
        }
        return r;
    }

    @Override
    public V get(K key) {
        Node lookup = search(root, key);
        if (lookup != null) {
            return lookup.val;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = insert(root, key, value);
        size++;
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node r) {
        if (r == null) {
            return;
        }
        if (r.leftchild == null && r.rightchild == null) {
            printNode(r);
        } else if (r.rightchild == null) {
            printInOrder(r.leftchild);
            printNode(r);
        } else if (r.leftchild == null) {
            printNode(r);
            printInOrder(r.rightchild);
        } else {
            printInOrder(r.leftchild);
            printNode(r);
            printInOrder(r.rightchild);
        }
    }

    private void printNode(Node r) {
        System.out.print(r.key);
        System.out.println(": " + r.val);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
