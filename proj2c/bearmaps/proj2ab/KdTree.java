package bearmaps.proj2ab;

import java.util.List;


public class KdTree implements PointSet {

    private static final boolean rootOri = false;
    private Node root = null;

    public KdTree(List<Point> points) {
        for (Point p: points) {
            root = insert(root, p, rootOri);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        Node best = nearestHelper(root, goal, root, rootOri);
        return best.getPoint();
    }

    private Node nearestHelper(Node n, Point goal, Node best, boolean o) {
        if (n == null) return best;
        if (n.distance(goal) < best.distance(goal)) best = n;
        Node goodSide;
        Node badSide;

        int cmp = new Node(goal, o).compareTo(n);
        if (cmp < 0) {
            goodSide = n.leftChild;
            badSide = n.rightChild;
        } else {
            goodSide = n.rightChild;
            badSide = n.leftChild;
        }
        best = nearestHelper(goodSide, goal, best, !o);
        if (isUseful(n, goal, best, o))
            best = nearestHelper(badSide, goal, best, !o);
        return best;
    }

    private boolean isUseful(Node n, Point goal, Node best, boolean o) {
        double nearest = best.distance(goal);
        double distOfbadSide;
        if (o) {
            distOfbadSide = Math.pow(n.getPoint().getY() - goal.getY(), 2);
        } else {
            distOfbadSide = Math.pow(n.getPoint().getX() - goal.getX(), 2);
        }
        return distOfbadSide < nearest;
    }

    private Node insert(Node n, Point p, boolean o) {
        if (n == null) { return new Node(p, o); }
        if (n.getPoint().equals(p)) { return n; }

        int cmp = new Node(p, o).compareTo(n);
        if (cmp < 0) {
            n.leftChild = insert(n.leftChild, p, !o);
        } else {
            n.rightChild = insert(n.rightChild, p, !o);
        }
        return n;
    }

    class Node implements Comparable {
        Point p;
        boolean orientation;
        Node leftChild;
        Node rightChild;

        Node(Point p, boolean o) {
            this.p = p;
            this.orientation = o;
            leftChild = null;
            rightChild = null;
        }

        Point getPoint() { return this.p; }
        double distance(Point op) {
            return Point.distance(this.p, op);
        }

        @Override
        public int compareTo(Object o) {
            if (orientation) {
                return Double.compare(p.getY(), ((Node) o).getPoint().getY());
            } else {
                return Double.compare(p.getX(), ((Node) o).getPoint().getX());
            }
        }
    }
}
