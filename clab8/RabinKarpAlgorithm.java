public class RabinKarpAlgorithm {


    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        if (input.length() < pattern.length()) { return -1; }
        RollingString tar = new RollingString(input.substring(0, pattern.length()), pattern.length());
        RollingString pat = new RollingString(pattern, pattern.length());
        for (int index=0; index<=input.length()-pattern.length(); index++) {
            if (tar.hashCode() == pat.hashCode() && tar.equals(pat)) {
                return index;
            }
            if (index!=input.length()-pattern.length()) {
                tar.addChar(input.charAt(index + pattern.length()));
            }
        }
        return -1;
    }
}
