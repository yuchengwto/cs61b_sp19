import org.junit.Test;
import static org.junit.Assert.*;
public class FlikTest {

    @Test
    public void testIsSameNumber(){
        Integer a = 1000;
        Integer b = 2000;
        Integer c = 2000;
        boolean isSameab = Flik.isSameNumber(a, b);
        boolean isSameab_exp = false;
        assertEquals(isSameab, isSameab_exp);

        boolean isSamebc = Flik.isSameNumber(b, c);
        boolean isSamebc_exp = true;
        assertEquals(isSamebc, isSamebc_exp);
    }

}
