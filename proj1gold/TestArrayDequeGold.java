import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testRandom() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();

        String msg = "";
        for (int i=0; i!=10 ; i++) {
            if (StdRandom.uniform() > 0.5) {
                sad.addFirst(i);
                ads.addFirst(i);
                msg += "addFirst(" + i + ")\n";
            } else {
                sad.addLast(i);
                ads.addLast(i);
                msg += "addLast(" + i + ")\n";
            }
        }

        for (int i=0; i!=10; i++) {
            if (StdRandom.uniform() > 0.5) {
                assertEquals(msg+i+"th removeFirst()\n", sad.removeFirst(), ads.removeFirst());
            } else {
                assertEquals(msg+i+"th removeLast()\n", sad.removeLast(), ads.removeLast());
            }
        }
    }
}
