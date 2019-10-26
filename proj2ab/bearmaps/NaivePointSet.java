package bearmaps;

import java.util.List;
import java.util.Set;

public class NaivePointSet implements PointSet {

    List<Point> points;

    @Override
    public Point nearest(double x, double y) {
        double nearest = Double.POSITIVE_INFINITY;
        Point np = new Point(0, 0);
        for (Point p: points) {
            double dist = Point.distance(p, new Point(x, y));
            if (nearest > dist) {
                nearest = dist;
                np = p;
            }
        }
        return np;
    }

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }



}
