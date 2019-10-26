public class OffByN implements CharacterComparator {
    private int offCount;

    public OffByN(int N) {
        this.offCount = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        if (diff == offCount || diff == -offCount)
            return true;
        return false;
    }
}
