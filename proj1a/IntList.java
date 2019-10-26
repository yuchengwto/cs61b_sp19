public class IntList {
    public int first;
    public IntList rest;
    public IntList (int f, IntList r){
        this.first = f;
        this.rest = r;
    }

    public void skippify(){
        IntList p = this;
        int n = 1;
        while (p != null){
            IntList next = p.rest;
            for (int i=0; i!=n; i++){
                next = next.rest;
                if (next == null){return; }
            }
            p.rest = next;
            p = next;
            n++;
        }
    }

    public static IntList ilsans(IntList x, int y){
        if (x == null){return null;}
        if (x.first == y){
            return ilsans(x.rest, y);
        }
        return new IntList(x.first, ilsans(x.rest, y));
    }

    public static IntList dilsans(IntList x, int y){
        if (x == null){
            return null;
        }
        x.rest = dilsans(x.rest, y);
        if (x.first == y){
            return x.rest;
        }
        return x;
    }
}