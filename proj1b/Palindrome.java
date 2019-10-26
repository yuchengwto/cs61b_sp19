import java.util.stream.StreamSupport;

public class Palindrome {

    public Deque<Character> wordToDeque(String word){
        Deque<Character> d = new LinkedListDeque<>();
        for (int i = 0; i != word.length(); i++){
            d.addLast(word.charAt(i));
        }
        return d;
    }

//    public boolean isPalindrome(String word) {
//        String reverse = "";
//        for (int i = word.length()-1; i>=0; i--)
//            reverse += word.charAt(i);
//        return word.equals(reverse);
//    }

//    public boolean isPalindrome(String word) {
//        Deque d = wordToDeque(word);
//        String rev = "";
//        for (int i=0; i!=word.length(); i++)
//            rev += d.removeLast();
//        return word.equals(rev);
//    }

    public boolean isPalindrome(String word){
        if (word.length() <= 1)
            return true;

        Deque<Character> d = wordToDeque(word);
        return isPalindromeHelper(d);
    }

    private boolean isPalindromeHelper(Deque d) {
        if (d.size() <= 1)
            return true;
        else if (d.removeFirst() == d.removeLast())
            return isPalindromeHelper(d);
        else
            return false;
    }

    public boolean isPalindrome(String word, CharacterComparator cc){
        if (word.length() <= 1)
            return true;
        Deque<Character> d = wordToDeque(word);
        return isPalindromeHelper(d, cc);
    }

    public boolean isPalindromeHelper(Deque d, CharacterComparator cc) {
        if (d.size() <= 1)
            return true;
        else if (cc.equalChars((char) d.removeFirst(), (char) d.removeLast()))
            return isPalindromeHelper(d, cc);
        else
            return false;
    }


}
