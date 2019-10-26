package bearmaps;

import edu.princeton.cs.introcs.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {

    private static final Random r = new Random(2019);

    @Test
    public void testNaive() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0);
        assertEquals(3.3, ret.getX(), 1e-4);
        assertEquals(4.4, ret.getY(), 1e-4);
    }

    private static KDTree buildLectureTree() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        return kd;
    }

    @Test
    /** test code by using examples from the nearest slides **/
    public void testNearestDemoSlides() {
        KDTree kd = buildLectureTree();
        Point actual = kd.nearest(0, 7);
        Point expect = new Point(1, 5);
        assertEquals(expect, actual);
    }

    private Point getRandomPoint() {
        double x = r.nextDouble()*10;
        double y = r.nextDouble()*10;
        return new Point(x, y);
    }

    private List<Point> getRandomPoints(int n) {
        int i = 0;
        List<Point> points = new ArrayList<>(n);
        while (i != n) {
            points.add(getRandomPoint());
            i++;
        }
        return points;
    }

    private void testRandomNPointsMQueries(int n, int m) {
        List<Point> points = getRandomPoints(n);
        List<Point> testPoints = getRandomPoints(m);
        KDTree kd = new KDTree(points);
        NaivePointSet nps = new NaivePointSet(points);

        for (Point p: testPoints) {
            Point actual = kd.nearest(p.getX(), p.getY());
            Point expect = nps.nearest(p.getX(), p.getY());
            assertEquals(expect, actual);
        }
    }

    private void testTimingNPointsMQueries(int n, int m) {
        List<Point> points = getRandomPoints(n);
        List<Point> testPoints = getRandomPoints(m);
        KDTree kd = new KDTree(points);
        NaivePointSet nps = new NaivePointSet(points);

        Stopwatch swkdt = new Stopwatch();
        for (Point p: testPoints) {
            kd.nearest(p.getX(), p.getY());
        }
        double kdTime = swkdt.elapsedTime();

        Stopwatch swnps = new Stopwatch();
        for (Point p: testPoints) {
            nps.nearest(p.getX(), p.getY());
        }
        double npsTime = swnps.elapsedTime();

        System.out.println("kdTime consume: "+kdTime+".");
        System.out.println("npsTime consume: "+npsTime+".");
        assertTrue(kdTime/npsTime < 0.1);
    }

    @Test
    public void testRandomPoints() {
        testRandomNPointsMQueries(100000, 10000);
    }

    @Test
    public void testTimingPoints() {
        testTimingNPointsMQueries(100000, 10000);
    }
}
