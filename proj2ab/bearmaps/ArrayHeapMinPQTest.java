package bearmaps;

import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;
import org.junit.Test;

import java.lang.reflect.Array;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrayHeapMinPQTest {

    @Test
    public void testSmallest() {
       ArrayHeapMinPQ<String> testPQ = new ArrayHeapMinPQ<>();
       testPQ.add("loveyou", 2.5);
       testPQ.add("leileleile", 0.0);
       testPQ.add("usausa", 9.9);
       assertEquals("leileleile", testPQ.getSmallest());
       assertEquals("leileleile", testPQ.removeSmallest());
       assertEquals("loveyou", testPQ.removeSmallest());
       assertEquals("usausa", testPQ.getSmallest());
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<String> testPQ = new ArrayHeapMinPQ<>();
        testPQ.add("fuckyou", 98);
        testPQ.add("loveyou", 78);
        testPQ.add("bibibi", 4.4);
        testPQ.changePriority("loveyou", 0);
        assertEquals("loveyou", testPQ.getSmallest());
    }

    @Test
    public void testSpeed() {
        ArrayHeapMinPQ<Integer> myPQ = new ArrayHeapMinPQ<>();
        Stopwatch sw3 = new Stopwatch();
        for (int i = 0; i!=1000000; i++) {
            int j = 1000000-i;
            myPQ.add(j, j);
        }
        System.out.println("myPQ add function consume: "+ sw3.elapsedTime()+ " s.");

        NaiveMinPQ<Integer> naivePQ = new NaiveMinPQ<>();
        Stopwatch sw4 = new Stopwatch();
        for (int i = 0; i!=1000000; i++) {
            int j = 1000000-i;
            naivePQ.add(j, j);
        }
        System.out.println("naivePQ add function consume: "+ sw4.elapsedTime()+ " s.");

        Stopwatch sw1 = new Stopwatch();
        for (int i = 0; i!=1000; i++) {
            int j = (int) (Math.random() * 1000000);
            myPQ.contains(j);
        }
        double timeSpent1 = sw1.elapsedTime();
        System.out.println("myPQ contains function consume: "+ sw1.elapsedTime()+ " s.");

        Stopwatch sw2 = new Stopwatch();
        for (int i = 0; i!=1000; i++) {
            int j = (int) (Math.random() * 1000000);
            naivePQ.contains(j);
        }
        double timeSpent2 = sw2.elapsedTime();
        System.out.println("naivePQ contains function consume: "+ sw2.elapsedTime()+ " s.");

        assertTrue(timeSpent1 / timeSpent2 < 0.01);
    }

}
