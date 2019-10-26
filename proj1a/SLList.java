public class SLList {
    private class IntNode {
        int item;
        IntNode next;
        IntNode(int item, IntNode next){
            this.item = item;
            this.next = next;
        }
    }

    private IntNode first;

    public void addFirst(int x){
        first = new IntNode(x, first);
    }

    public void insert(int x, int pos){
        if (first == null || pos == 0){
            this.addFirst(x);
            return;
        }

        IntNode p = first;
        int index = 1;
        while (index != pos && p.next != null){
            index++;
            p = p.next;
        }
        p.next = new IntNode(x, p.next);
    }

    public int getFirst(){
        return first.item;
    }

    public void reverse_recursively(){
        first = reverse_helper(first);
    }

    public IntNode reverse_helper(IntNode lst){
        if (lst == null || lst.next == null)
            return lst;

        IntNode temp = lst.next;
        IntNode endoflist = reverse_helper(lst.next);
        temp.next = lst;
        lst.next = null;
        return endoflist;
    }

    public void reverse_iteratively(){
        if (first == null || first.next == null)
            return;

        IntNode p = first.next;
        first.next = null;
        IntNode temp;

        while (p != null){
            temp = p.next;
            p.next = first;
            first = p;
            p = temp;
        }

    }



    public static void main(String[] args){
        SLList sl = new SLList();
        sl.addFirst(1);
        sl.addFirst(2);
        sl.addFirst(3);
        sl.addFirst(4);
        System.out.println(sl.getFirst());
        sl.reverse_recursively();
        System.out.println(sl.getFirst());
        sl.reverse_iteratively();
        System.out.println(sl.getFirst());
    }


}
