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

public class BruteCollinearPoints {
    private final int num;
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        int count = 0;
        if (points == null) throw new IllegalArgumentException();
        for (Point p : points)
            if (p == null) throw new IllegalArgumentException();

        Point[] inputPoints = points.clone();
        Arrays.sort(inputPoints);
        for (int i = 0; i < inputPoints.length - 1; i++) {
            if (inputPoints[i].compareTo(inputPoints[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        int n = points.length;
        for (int p = 0; p < n - 3; p++)
            for (int q = p + 1; q < n - 2; q++)
                for (int r = q + 1; r < n - 1; r++) {
                    if (inputPoints[p].slopeTo(inputPoints[q]) == inputPoints[p]
                            .slopeTo(inputPoints[r]))
                        for (int s = r + 1; s < n; s++) {
                            if (inputPoints[p].slopeTo(inputPoints[q]) == inputPoints[p]
                                    .slopeTo(inputPoints[s])) {
                                count += 1;
                                lineSegments.add(new LineSegment(inputPoints[p], inputPoints[s]));
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
            if (points[i] == null) throw new IllegalArgumentException();
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
