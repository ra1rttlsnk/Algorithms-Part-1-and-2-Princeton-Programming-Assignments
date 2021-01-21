/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> pointSet;

    public PointSET() {
        pointSet = new SET<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> inRect = new SET<>();
        for (Point2D p : pointSet) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax())
                if (p.y() >= rect.ymin() && p.y() <= rect.ymax())
                    inRect.add(p);
        }

        return inRect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D nearestPoint = null;
        double leastDistance = Double.POSITIVE_INFINITY;
        double dist;
        for (Point2D point : pointSet) {
            dist = p.distanceSquaredTo(point);
            if (dist < leastDistance) {
                leastDistance = dist;
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }

   /* public static void main(String[] args) {

    } */
}
