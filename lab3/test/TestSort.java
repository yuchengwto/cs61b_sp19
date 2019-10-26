import org.junit.Test;

public class TestSort {
    public static void testSort(){
        String[] input = {"i", "have","an", "egg"};
        String[] expected = {"an", "egg", "have", "i"};

        Sort.sort(input);

        if (!java.util.Arrays.equals(input, expected)){
            System.out.println("error! there seems to be a problem with Sort.sort.");
        }

    }


    public static void main(String[] args){
        testSort();

    }
}