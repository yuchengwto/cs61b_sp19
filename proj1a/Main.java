public class Main {

    public static int[] flatten(int[][] x) {
        int totalLength = 0;

        for (int[] i : x) {
            for (int j : i) {
                totalLength++;
            }
        }

        int[] a = new int[totalLength];
        int aIndex = 0;

        for (int[] i : x) {
            for (int j : i) {
                a[aIndex] = j;
                aIndex++;
            }
        }
        return a;
    }
}
