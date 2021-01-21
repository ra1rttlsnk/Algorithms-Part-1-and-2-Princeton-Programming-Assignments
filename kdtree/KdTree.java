/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private Node parent;
        private Node right;
        private Node left;
        private final Point2D point;

        public Node(Point2D p) {
            point = p;
            parent = null;
            right = null;
            left = null;
        }


        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public double x() {
            return point.x();
        }

        public double y() {
            return point.y();
        }

        public Point2D getPoint() {
            return point;
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        if (root == null) {
            root = new Node(p);
            size++;
            return;
        }

        Node current = root;
        Node prev;
        int dim = 0;
        while (current != null) {
            if (dim % 2 == 0) {
                if (p.x() <= current.x()) {
                    prev = current;
                    current = current.left;
                    if (current == null) {
                        prev.left = new Node(p);
                        prev.left.setParent(prev);
                    }
                }
                else {
                    prev = current;
                    current = current.right;
                    if (current == null) {
                        prev.right = new Node(p);
                        prev.right.setParent(prev);
                    }
                }
                dim++;
            }
            else {
                if (p.y() <= current.y()) {
                    prev = current;
                    current = current.left;
                    if (current == null) {
                        prev.left = new Node(p);
                        prev.left.setParent(prev);
                    }
                }
                else {
                    prev = current;
                    current = current.right;
                    if (current == null) {
                        prev.right = new Node(p);
                        prev.right.setParent(prev);
                    }
                }
                dim++;
            }
        }
        size++;
    }


    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node current = root;
        int dim = 0;

        while (current != null && !current.getPoint().equals(p)) {
            if (dim % 2 == 0) {
                if (p.x() <= current.x()) {
                    current = current.left;
                }
                else {
                    current = current.right;
                }
                dim++;
            }
            else {
                if (p.y() <= current.y()) {
                    current = current.left;
                }
                else {
                    current = current.right;
                }
                dim++;
            }
        }

        return current != null;
    }

    public void draw() {
        Node current = root;
        int dim = 0;
        drawLine(current, dim, 0.0, 1.0, 1.0, 0.0);

    }


    private void drawLine(Node c, int d, double left, double r, double t, double b) {
        if (c.getParent() == null) {
            new Point2D(c.x(), t).drawTo(new Point2D(c.x(), b));
        }
        if (d % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(c.x(), t).drawTo(new Point2D(c.x(), b));
            c.getPoint().draw();
            if (c.left != null) drawLine(c.left, d + 1, left, c.x(), t, b);
            if (c.right != null) drawLine(c.right, d + 1, c.x(), r, t, b);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(left, c.y()).drawTo(new Point2D(r, c.y()));
            c.getPoint().draw();
            if (c.left != null) drawLine(c.left, d + 1, left, r, c.y(), b);
            if (c.right != null) drawLine(c.right, d + 1, left, r, t, c.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (root == null) return null;
        RectHV inputRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        ArrayList<Point2D> inRect = new ArrayList<>();
        if (rect.intersects(inputRect)) {
            if (rect.contains(root.getPoint())) inRect.add(root.getPoint());
            if (root.left != null) check(root.left, 1, rect, new RectHV(inputRect.xmin(),
                                                                        inputRect.ymin(),
                                                                        root.x(),
                                                                        inputRect.ymax()),
                                         inRect);
            if (root.right != null) check(root.right, 1, rect, new RectHV(root.x(),
                                                                          inputRect.ymin(),
                                                                          inputRect.xmax(),
                                                                          inputRect.ymax()),
                                          inRect);

        }

        return inRect;
    }

    private void check(Node n, int dim, RectHV rect, RectHV inputRect, ArrayList<Point2D> inRect) {
        if (rect.intersects(inputRect)) {
            if (rect.contains(n.getPoint())) inRect.add(n.getPoint());
            if (dim % 2 == 0) {
                if (n.left != null) check(n.left, dim + 1, rect,
                                          new RectHV(inputRect.xmin(), inputRect.ymin(), n.x(),
                                                     inputRect.ymax()), inRect);
                if (n.right != null) check(n.right, dim + 1, rect,
                                           new RectHV(n.x(), inputRect.ymin(),
                                                      inputRect.xmax(), inputRect.ymax()), inRect);
            }

            else {
                if (n.left != null) check(n.left, dim + 1, rect,
                                          new RectHV(inputRect.xmin(), inputRect.ymin(),
                                                     inputRect.xmax(),
                                                     n.y()), inRect);
                if (n.right != null) check(n.right, dim + 1, rect,
                                           new RectHV(inputRect.xmin(), n.y(),
                                                      inputRect.xmax(), inputRect.ymax()), inRect);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        Point2D n;
        if (p.x() <= root.x()) {
            n = checkNearest(root.left, 1, new RectHV(0.0, 0.0, root.x(), 1.0), p, root.getPoint(),
                             p.distanceSquaredTo(root.getPoint()));
            n = checkNearest(root.right, 1, new RectHV(root.x(), 0.0, 1.0, 1.0), p, n,
                             p.distanceSquaredTo(n));
        }

        else {
            n = checkNearest(root.right, 1, new RectHV(root.x(), 0.0, 1.0, 1.0), p, root.getPoint(),
                             p.distanceSquaredTo(root.getPoint()));
            n = checkNearest(root.left, 1, new RectHV(0.0, 0.0, root.x(), 1.0), p, n,
                             p.distanceSquaredTo(n));
        }

        return n;
    }

    private Point2D checkNearest(Node c, int dim, RectHV rect, Point2D p, Point2D n, double dist) {

        if (c == null) return n;
        if (c.getPoint().distanceSquaredTo(p) < dist) {
            n = c.getPoint();
            dist = n.distanceSquaredTo(p);
        }
        if (rect.contains(p) || rect.distanceSquaredTo(p) < dist) {
            if (dim % 2 == 0) {

                if (p.x() <= c.x()) {
                    n = checkNearest(c.left, dim + 1, new RectHV(rect.xmin(), rect.ymin(),
                                                                 c.x(), rect.ymax()), p, n, dist);
                    dist = p.distanceSquaredTo(n);
                    n = checkNearest(c.right, dim + 1, new RectHV(c.x(), rect.ymin(),
                                                                  rect.xmax(), rect.ymax()), p, n,
                                     dist);
                }
                else {
                    n = checkNearest(c.right, dim + 1, new RectHV(c.x(), rect.ymin(),
                                                                  rect.xmax(), rect.ymax()), p, n,
                                     dist);
                    dist = p.distanceSquaredTo(n);
                    n = checkNearest(c.left, dim + 1, new RectHV(rect.xmin(), rect.ymin(),
                                                                 c.x(), rect.ymax()), p, n, dist);

                }
            }

            else {
                if (p.y() <= c.y()) {
                    n = checkNearest(c.left, dim + 1, new RectHV(rect.xmin(), rect.ymin(),
                                                                 rect.xmax(), c.y()), p, n, dist);
                    dist = p.distanceSquaredTo(n);
                    n = checkNearest(c.right, dim + 1, new RectHV(rect.xmin(), c.y(),
                                                                  rect.xmax(), rect.ymax()), p, n,
                                     dist);
                }
                else {
                    n = checkNearest(c.right, dim + 1, new RectHV(rect.xmin(), c.y(),
                                                                  rect.xmax(), rect.ymax()), p, n,
                                     dist);

                    dist = p.distanceSquaredTo(n);
                    n = checkNearest(c.left, dim + 1, new RectHV(rect.xmin(), rect.ymin(),
                                                                 rect.xmax(), c.y()), p, n, dist);

                }

            }
        }
        return n;
    }

    // public static void main(String[] args) {

    //}
}
