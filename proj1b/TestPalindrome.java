import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        boolean ispa = palindrome.isPalindrome("racecar");
        boolean notpa = palindrome.isPalindrome("fuck");
        boolean ispa1 = palindrome.isPalindrome("");
        boolean ispa2 = palindrome.isPalindrome("a");
        assertTrue(ispa);
        assertFalse(notpa);
        assertTrue(ispa1);
        assertTrue(ispa2);
    }

}