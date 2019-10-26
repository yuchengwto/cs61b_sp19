package hw3.hash;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {

        int[] buckets = new int[M];
        int index;
        for (Oomage i: oomages) {
            index = (i.hashCode() & 0x7FFFFFFF) % M;
            buckets[index] += 1;
        }

        int upperBound = (int) (oomages.size() / 2.5);
        int lowerBound = oomages.size() / 50;
        for (int i=0; i!=M; i++) {
            if (buckets[i] > upperBound || buckets[i] < lowerBound) {return false;}
        }

        return true;
    }
}
