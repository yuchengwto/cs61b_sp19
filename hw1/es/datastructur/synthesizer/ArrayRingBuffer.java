package es.datastructur.synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     * @param x
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        } else {
            rb[last] = x;
            last = (last + 1) % capacity();
            fillCount++;
        }
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            T item = rb[first];
            rb[first] = null;
            first = (first + 1) % capacity();
            fillCount--;
            return item;
        }
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            return rb[first];
        }
    }

    private class ArrayRingBufferIter implements Iterator<T> {
        private int Pos;
        ArrayRingBufferIter() { Pos = first; }

        @Override
        public boolean hasNext() {
            return Pos < last;
        }

        @Override
        public T next() {
            T item = rb[Pos];
            Pos = (Pos + 1) % capacity();
            return item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIter();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if ( !(o instanceof ArrayRingBuffer) ) {
            return false;
        }
        ArrayRingBuffer<T> obj = (ArrayRingBuffer<T>) o;

        if (obj.fillCount() != this.fillCount()) {
            return false;
        }
        Iterator<T> iterthis = this.iterator();
        Iterator<T> iterobj = obj.iterator();

        while (iterthis.hasNext() && iterobj.hasNext()) {
            if ( !iterthis.next().equals(iterobj.next()) ) {
                return false;
            }
        }
        return true;
    }
}
