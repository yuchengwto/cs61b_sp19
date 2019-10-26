package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        //ArrayRingBuffer arb = new ArrayRingBuffer(10);
        ArrayRingBuffer<Integer> a1 = new ArrayRingBuffer<>(10);
        for (int i=0; i!=a1.capacity(); i++) {
            a1.enqueue(i);
        }

        ArrayRingBuffer<Integer> a2 = new ArrayRingBuffer<>(10);
        for (int i=0; i!=a2.capacity(); i++) {
            a2.enqueue(i);
        }

        boolean res = a1.equals(a2);
        assertTrue(res);

    }
}
