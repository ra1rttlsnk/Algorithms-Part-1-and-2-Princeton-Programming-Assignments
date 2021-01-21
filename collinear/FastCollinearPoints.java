/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final int num;
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        boolean subsegment;
        int count = 0;
        if (points == null) throw new IllegalArgumentException();
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();

        Point[] inputPoints = points.clone();
        Arrays.sort(inputPoints);

        for (int i = 0; i < points.length - 1; i++) {
            if (inputPoints[i].compareTo(inputPoints[i + 1]) == 0)
                throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            Arrays.sort(inputPoints);
            Arrays.sort(inputPoints, points[i].slopeOrder());
            int a = 0;
            subsegment = false;
            for (int j = 0; j < points.length - 1; j++) {
                if (points[i].slopeTo(inputPoints[j]) == points[i].slopeTo(inputPoints[j + 1])) {
                    if (points[i].compareTo(inputPoints[j]) > 0)
                        subsegment = true;
                    a += 1;
                }

                else {
                    if (a >= 2) {
                        a = 0;

                        if (points[i].compareTo(inputPoints[j]) < 0 && !subsegment) {
                            lineSegments.add(new LineSegment(points[i], inputPoints[j]));
                            count += 1;
                        }
                        subsegment = false;
                    }
                    else {
                        a = 0;
                        subsegment = false;
                    }
                }
                if (j == points.length - 2) {
                    if (a >= 2) {
                        a = 0;
                        if (points[i].compareTo(inputPoints[j]) < 0 && !subsegment) {
                            lineSegments.add(new LineSegment(points[i], inputPoints[j + 1]));
                            count += 1;
                        }
                        subsegment = false;
                    }
                }
            }
        }
        num = count;
    }

    public int numberOfSegments() {
        return num;
    }

    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); i++) {
            segs[i] = lineSegments.get(i);
        }

        return segs;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
