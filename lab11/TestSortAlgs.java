import edu.princeton.cs.algs4.Queue;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<Integer> testQ = new Queue<>();
        testQ.enqueue(5);
        testQ = QuickSort.quickSort(testQ);
        assertTrue(isSorted(testQ));
        testQ.enqueue(3);
        testQ.enqueue(9);
        testQ.enqueue(7);
        testQ.enqueue(1);
        testQ.enqueue(22);
        testQ.enqueue(8);
        testQ = QuickSort.quickSort(testQ);
        assertTrue(isSorted(testQ));
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> testQ = new Queue<>();
        testQ.enqueue(7);
        testQ = MergeSort.mergeSort(testQ);
        assertTrue(isSorted(testQ));
        testQ.enqueue(3);
        testQ.enqueue(1);
        testQ.enqueue(6);
        testQ.enqueue(86);
        testQ.enqueue(222);
        testQ.enqueue(4);
        testQ = MergeSort.mergeSort(testQ);
        assertTrue(isSorted(testQ));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
