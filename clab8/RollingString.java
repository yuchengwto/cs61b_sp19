import java.util.ArrayList;
import java.util.List;

/**
 * A String-like class that allows users to add and remove characters in the String
 * in constant time and have a constant-time hash function. Used for the Rabin-Karp
 * string-matching algorithm.
 */
class RollingString{
    private List<Character> rollingString;
    private long hashValue;

    /**
     * Number of total possible int values a character can take on.
     * DO NOT CHANGE THIS.
     */
    static final int UNIQUECHARS = 128;

    /**
     * The prime base that we are using as our mod space. Happens to be 61B. :)
     * DO NOT CHANGE THIS.
     */
    static final int PRIMEBASE = 6113;

    /**
     * Initializes a RollingString with a current value of String s.
     * s must be the same length as the maximum length.
     */
    public RollingString(String s, int length) {
        assert(s.length() == length);
        rollingString = new ArrayList<>();
        for (char i : s.toCharArray()) rollingString.add(i);
        hashValue = 0;
        for (int i = 0; i != length; i++) {
            int charValue = (int) rollingString.get(i);
            hashValue += charValue * Math.pow(UNIQUECHARS, length-i-1);
        }
    }

    /**
     * Adds a character to the back of the stored "string" and 
     * removes the first character of the "string". 
     * Should be a constant-time operation.
     */
    public void addChar(char c) {
        int removeCharValue = (int) rollingString.remove(0);
        int addCharValue = (int) c;
        rollingString.add(c);
        hashValue -= removeCharValue * Math.pow(UNIQUECHARS, length()-1);
        hashValue = hashValue * UNIQUECHARS + addCharValue;
    }


    /**
     * Returns the "string" stored in this RollingString, i.e. materializes
     * the String. Should take linear time in the number of characters in
     * the string.
     */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (Character i: rollingString) {
            strb.append(i);
        }
        return "" + strb;
    }

    /**
     * Returns the fixed length of the stored "string".
     * Should be a constant-time operation.
     */
    public int length() {
        return rollingString.size();
    }


    /**
     * Checks if two RollingStrings are equal.
     * Two RollingStrings are equal if they have the same characters in the same
     * order, i.e. their materialized strings are the same.
     */
    @Override
    public boolean equals(Object o) {
        RollingString obj = (RollingString) o;
        for (int i=0; i!=length(); i++) {
            if (rollingString.get(i) != obj.rollingString.get(i)) { return false; }
        }
        return true;
    }

    /**
     * Returns the hashcode of the stored "string".
     * Should take constant time.
     */
    @Override
    public int hashCode() {
        return (int) (hashValue % PRIMEBASE);
    }
}
