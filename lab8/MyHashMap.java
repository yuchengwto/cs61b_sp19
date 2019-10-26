
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {

            private static final int initialSize = 16;
            private static final double loadFactor = 0.75;
            private int capacity;
            private int threshold;
            private int size;
            private HashSet<K> keySet;
            private BucketEntity<K, V>[] buckets;

            public MyHashMap(){
                capacity = initialSize;
                threshold = (int) (capacity*loadFactor);
                buckets = new BucketEntity[capacity];
                size = 0;
                keySet = new HashSet<>();
            }
            public MyHashMap(int initialSize){
                capacity = initialSize;
                threshold = (int) (capacity*loadFactor);
                buckets = new BucketEntity[capacity];
                size = 0;
                keySet = new HashSet<>();
            }
            public MyHashMap(int initialSize, double loadFactor){
                capacity = initialSize;
                threshold = (int) (capacity*loadFactor);
                buckets = new BucketEntity[capacity];
                size = 0;
                keySet = new HashSet<>();
            }

            class BucketEntity<K, V> {

                K key;
                V value;
                int length;
                BucketEntity<K, V> next;

                BucketEntity(K key, V value, BucketEntity<K, V> next) {
                    this.key = key;
                    this.value = value;
                    this.next = next;
                    this.length = 1;
                }

        BucketEntity<K, V> get(BucketEntity<K, V> entity, K key) {
            if (entity == null) {
                return null;
            }
            if (entity.key.equals(key)) {
                return entity;
            } else {
                return get(entity.next, key);
            }
        }

        BucketEntity<K, V> put(BucketEntity<K, V> entity, K key, V value) {
            if (entity == null) {
                this.length++;
                return new BucketEntity<>(key, value, null);
            }
            if (entity.key.equals(key)) {
                entity.value = value;
                return entity;
            }
            entity.next = put(entity.next, key, value);
            return entity;
        }

        V getValue() {return this.value;}
        K getKey() {return this.key; }
        int getLength() {return this.length; }
    }

    @Override
    public void clear() {
        buckets = new BucketEntity[capacity];
        keySet.clear();
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    @Override
    public V get(K key) {
        int index = getBucketHash(key, buckets.length);
        BucketEntity<K, V> bucket = buckets[index];
        if (bucket == null) {
            return null;
        } else {
            BucketEntity<K, V> target = bucket.get(bucket, key);
            if (target == null) { return null; }
            else { return target.getValue(); }
        }
    }

    private int getBucketHash(K key, int length) {
        if (key == null) {throw new IllegalArgumentException();}
        return (key.hashCode() & 0x7FFFFFFF) % length;
    }

    @Override
    public int size() {
        return this.size;
    }

    public void resize() {
        capacity *= 2;
        MyHashMap<K, V> newHashMap = new MyHashMap<>(capacity);
        for (BucketEntity<K, V> i: this.buckets) {
            if (i == null) { continue; }
            BucketEntity<K, V> bucket = i;
            while (bucket != null) {
                newHashMap.put(bucket.getKey(), bucket.getValue());
                bucket = bucket.next;
            }
        }

        this.buckets = newHashMap.buckets;
        this.threshold = (int) (capacity*loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketHash(key, buckets.length);
        BucketEntity<K, V> bucket = buckets[index];
        if (bucket == null) {
            buckets[index] = new BucketEntity<>(key, value, null);
            size++;
        } else {
            int oldLength = bucket.getLength();
            bucket = bucket.put(bucket, key, value);
            if (bucket.getLength() - oldLength == 1) { size++; }
        }

        keySet.add(key);
        if ( this.size() > threshold ) { resize(); }
    }

    @Override
    public Set<K> keySet() {
        return keySet;
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
        return keySet.iterator();
    }
}
