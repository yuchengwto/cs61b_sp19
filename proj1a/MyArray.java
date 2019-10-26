public class MyArray {

    public static int[] insert(int[] arr, int item, int position){
        int[] newarr = new int[arr.length + 1];
        System.arraycopy(arr, 0, newarr, 0, position);
        newarr[position] = item;
        System.arraycopy(arr, position, newarr, position+1, arr.length - position);
        return newarr;
    }

    public static void reverse(int[] arr){
        for (int index=0 ; index < arr.length/2; index++){
            int temp = arr[index];
            arr[index] = arr[arr.length-index-1];
            arr[arr.length-index-1] = temp;
        }
    }

    public static int[] replicate(int[] arr){
        int sum = 0;
        for (int i: arr)
            sum += i;
        int[] repliarr = new int[sum];
        int index = 0;
        for (int i: arr){
            for (int cnt=0; cnt!=i; cnt++){
                repliarr[index] = i;
                index++;
            }
        }

        return repliarr;
    }


    public static void main(String[] args){
        int[] arr = {1, 2, 3, 4, 5};
        MyArray.reverse(arr);
        for (int i: arr)
            System.out.println(i);
        int[] narr = MyArray.replicate(arr);
        for (int i: narr)
            System.out.println(i);
    }

}
