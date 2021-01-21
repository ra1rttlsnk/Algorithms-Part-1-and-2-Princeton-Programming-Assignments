/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;

public class SeamCarver3 {
    private Picture inputPicture;
    private boolean isTransposed;

    public SeamCarver3(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        inputPicture = new Picture(picture);
    }

    private void transpose() {
        Picture transposedPicture = new Picture(h(), w());
        for (int i = 0; i < h(); i++)
            for (int j = 0; j < w(); j++) {
                transposedPicture.set(i, j, inputPicture.get(j, i));
            }
        inputPicture = transposedPicture;
    }

    private int serial(int x, int y) {
        // considering source and sink (+1)
        return y * w() + x + 1;
    }

    private int xIndex(int s) {
        // considering source and sink (s-1)
        return (s - 1) % w();
    }

    public Picture picture() {
        if (isTransposed) {
            isTransposed = false;
            transpose();
            return new Picture(inputPicture);
        }
        return new Picture(inputPicture);
    }

    public int width() {
        if (isTransposed)
            return h();
        return w();
    }

    public int height() {
        if (isTransposed)
            return w();
        return h();
    }

    private int w() {
        return inputPicture.width();
    }

    private int h() {
        return inputPicture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x > w() - 1) throw new IllegalArgumentException();
        if (y < 0 || y > h() - 1) throw new IllegalArgumentException();
        if (x == 0 || x == w() - 1 ||
                y == 0 || y == h() - 1) {
            return 1000.0;
        }
        Color colorLeft = inputPicture.get(x - 1, y);
        Color colorRight = inputPicture.get(x + 1, y);
        Color colorUp = inputPicture.get(x, y - 1);
        Color colorDown = inputPicture.get(x, y + 1);

        int deltaX2 = delta2(colorLeft, colorRight);
        int deltaY2 = delta2(colorUp, colorDown);

        return Math.sqrt((deltaX2 + deltaY2));

    }

    private int delta2(Color a, Color b) {
        int rx2 = (a.getRed() - b.getRed());
        rx2 *= rx2;
        int gx2 = (a.getGreen() - b.getGreen());
        gx2 *= gx2;
        int bx2 = (a.getBlue() - b.getBlue());
        bx2 *= bx2;

        return rx2 + gx2 + bx2;
    }

    public int[] findHorizontalSeam() {

        /* EdgeWeightedDigraph digraphHorizontal = new EdgeWeightedDigraph(width() * height());
        for (int x = 0; x < width() - 1; x++) {
            for (int y = 0; y < height(); y++) {
                if (y - 1 >= 0 && x + 1 <= width() - 1)
                    digraphHorizontal.addEdge(new DirectedEdge(serial(x, y), serial(x + 1, y - 1),
                                                               energy(x + 1, y - 1)));
                if (y + 1 <= height() - 1 && x + 1 <= width() - 1)
                    digraphHorizontal.addEdge(new DirectedEdge(serial(x, y), serial(x + 1, y + 1),
                                                               energy(x + 1, y + 1)));
                if (x + 1 <= width() - 1)
                    digraphHorizontal.addEdge(new DirectedEdge(serial(x, y), serial(x + 1, y),
                                                               energy(x + 1, y)));
            }
        }

        int[] yIndices = new int[width()];
        int start = StdRandom.uniform(0, height());
        AcyclicSP sp = new AcyclicSP(digraphHorizontal, serial(0, start));
        int destination = StdRandom.uniform(0, height());
        while (!sp.hasPathTo(serial(width() - 1, destination))) {
            destination = StdRandom.uniform(0, height());
        }
        yIndices[0] = start;
        Iterable<DirectedEdge> edges = sp.pathTo(serial(width() - 1, destination));
        int count = 1;
        for (DirectedEdge e : edges) {
            yIndices[count] = yIndex(e.to());
            count++;
        }

        return yIndices;*/
        if (!isTransposed) {
            isTransposed = true;
            transpose();
        }
        return findSeam();

    }

    public int[] findVerticalSeam() {
        if (isTransposed) {
            isTransposed = false;
            transpose();
        }
        return findSeam();
    }

    private int[] findSeam() {
        EdgeWeightedDigraph digraphVertical = new EdgeWeightedDigraph(w() * h() + 2);

        // find a way to select the best possible starting and ending point
        // that yield the lowest energy seam

        // Let's use a source and a sink!
        int startingPoint = 0;
        int endingPoint = w() * h() + 1;
        // Now, to add the edges
        for (int x = 0; x < w(); x++) {
            for (int y = 0; y < h(); y++) {
                if (x - 1 >= 0 && y + 1 <= h() - 1)
                    digraphVertical.addEdge(new DirectedEdge(serial(x, y), serial(x - 1, y + 1),
                                                             energy(x - 1, y + 1)));
                if (x + 1 <= w() - 1 && y + 1 <= h() - 1)
                    digraphVertical.addEdge(new DirectedEdge(serial(x, y), serial(x + 1, y + 1),
                                                             energy(x + 1, y + 1)));
                if (y + 1 <= h() - 1)
                    digraphVertical.addEdge(new DirectedEdge(serial(x, y), serial(x, y + 1),
                                                             energy(x, y + 1)));
                if (y == 0)
                    digraphVertical.addEdge(new DirectedEdge(startingPoint, serial(x, y), 0));
                if (y == h() - 1)
                    digraphVertical
                            .addEdge(new DirectedEdge(serial(x, y), endingPoint, 0));
            }
        }

        int[] xIndices = new int[h()];
        AcyclicSP sp = new AcyclicSP(digraphVertical, startingPoint);
        while (!sp.hasPathTo(endingPoint)) {
            endingPoint = StdRandom.uniform(0, w());
        }
        Iterable<DirectedEdge> edges = sp.pathTo(endingPoint);
        int count = 0;
        for (DirectedEdge e : edges) {
            if (e.to() != endingPoint) {
                xIndices[count] = xIndex(e.to());
                count++;
            }
        }

        return xIndices;
    }

    public void removeHorizontalSeam(int[] seam) {
        /* if (height() <= 1) throw new IllegalArgumentException();
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        Picture retargetedPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            boolean toggle = false;
            for (int y = 0; y < height() - 1; y++) {
                if (seam[x] < 0 || seam[x] > height() - 1) throw new IllegalArgumentException();
                if (x < width() - 1 && Math.abs(seam[x] - seam[x + 1]) > 1)
                    throw new IllegalArgumentException();
                if (seam[x] == y) toggle = true;
                if (toggle) retargetedPicture.set(x, y, inputPicture.get(x, y + 1));
                else retargetedPicture.set(x, y, inputPicture.get(x, y));
            }
        }

        inputPicture = retargetedPicture; */

        if (!isTransposed) {
            isTransposed = true;
            transpose();
        }
        removeSeam(seam);

    }

    public void removeVerticalSeam(int[] seam) {
        if (isTransposed) {
            isTransposed = false;
            transpose();
        }
        removeSeam(seam);
    }

    private void removeSeam(int[] seam) {
        if (w() <= 1) throw new IllegalArgumentException();
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != h()) throw new IllegalArgumentException();
        Picture retargetedPicture = new Picture(w() - 1, h());
        for (int y = 0; y < h(); y++) {
            boolean toggle = false;
            for (int x = 0; x < w() - 1; x++) {
                if (seam[y] < 0 || seam[y] > w() - 1) throw new IllegalArgumentException();
                if (y < h() - 1 && Math.abs(seam[y] - seam[y + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
                if (seam[y] == x) toggle = true;
                if (toggle) retargetedPicture.set(x, y, inputPicture.get(x + 1, y));
                else retargetedPicture.set(x, y, inputPicture.get(x, y));

            }
        }
        inputPicture = retargetedPicture;

    }
}
