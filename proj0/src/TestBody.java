

public class TestBody {
    public static void main(String[] args){
        Body a = new Body(1, 1, 1, 1, 1, "jupiter.gif");
        Body b = new Body(2,2,2,2,2, "jupiter.gif");
        double force = a.calcForceExertedBy(b);
        System.out.println(force);
    }
}