/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;

public class SeamCarver2 {
    private Picture inputPicture;
    private double[][] energies;
    private boolean isTransposed;
    private double[] distTo;
    private int[] edgeTo;

    public SeamCarver2(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        isTransposed = false;
        inputPicture = new Picture(picture);
        energies = new double[h()][w()];
        for (int i = 0; i < h(); i++)
            for (int j = 0; j < w(); j++)
                energies[i][j] = calculateEnergy(j, i);
    }

    private void transpose() {
        // double[][] transposed = new double[w()][h()];
        Picture transposedPicture = new Picture(h(), w());
        for (int i = 0; i < h(); i++)
            for (int j = 0; j < w(); j++) {
                // transposed[j][i] = energies[i][j];
                transposedPicture.set(i, j, inputPicture.get(j, i));
            }
        inputPicture = transposedPicture;
        // energies = transposed;
    }

    private int serial(int x, int y) {
        return y * w() + x + 1;
    }

    private int xIndex(int s) {
        return (s - 1) % w();
    }

    private int yIndex(int s) {
        return (s - 1) / w();
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

    private double calculateEnergy(int x, int y) {
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

    private double e(int serial) {
        return e(xIndex(serial), yIndex(serial));
    }

    private double e(int x, int y) {
        if (x < 0 || x > w() - 1) throw new IllegalArgumentException();
        if (y < 0 || y > h() - 1) throw new IllegalArgumentException();
        if (isTransposed) return energies[x][y];
        return energies[y][x];
    }

    public double energy(int x, int y) {
        return energies[y][x];
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
        distTo = new double[w() * h() + 2];
        edgeTo = new int[w() * h() + 2];
        edgeTo[0] = 0;
        distTo[0] = 0;
        for (int i = 1; i < w() * h() + 2; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < h() * w() + 1; i++)
            relax(i);
        int[] seam = new int[h()];
        int c = 0;
        for (int i : traverse()) {
            seam[c] = xIndex(i);
            c++;
        }
        return seam;
    }

    private void relax(int serial) {

        for (int adj : adj(serial)) {
            if (adj != w() * h() + 1) {
                if (distTo[adj] >= distTo[serial] + e(adj)) {
                    distTo[adj] = distTo[serial] + e(adj);
                    edgeTo[adj] = serial;
                }
            }
            else {
                if (distTo[adj] >= distTo[serial] + 0) {
                    distTo[adj] = distTo[serial] + 0;
                    edgeTo[adj] = serial;
                }
            }
        }
    }

    private Iterable<Integer> adj(int serial) {
        Queue<Integer> adj = new Queue<>();

        if (serial == 0) {
            for (int i = 1; i <= w(); i++) {
                adj.enqueue(i);
            }
            return adj;
        }

        int x = xIndex(serial);
        int y = yIndex(serial);

        if (y == h() - 1) {
            adj.enqueue(w() * h() + 1);
            return adj;
        }
        if (x - 1 >= 0 && y < h() - 1)
            adj.enqueue(serial(x - 1, y + 1));
        if (y < h() - 1)
            adj.enqueue(serial(x, y + 1));
        if (x + 1 <= w() - 1 && y < h() - 1)
            adj.enqueue(serial(x + 1, y + 1));

        return adj;

    }

    private Iterable<Integer> traverse() {
        Stack<Integer> shortestPath = new Stack<>();
        for (int e = edgeTo[w() * h() + 1]; e != 0; e = edgeTo[e]) {
            shortestPath.push(e);
        }
        return shortestPath;
    }

    public void removeHorizontalSeam(int[] seam) {

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

        double[][] newEnergies;
        if (isTransposed) newEnergies = new double[height() - 1][width()];
        else newEnergies = new double[height()][width() - 1];

        Picture retargetedPicture = new Picture(w() - 1, h());

        for (int y = 0; y < h(); y++) {
            boolean toggle = false;
            for (int x = 0; x < w() - 1; x++) {
                if (seam[y] < 0 || seam[y] > w() - 1) throw new IllegalArgumentException();
                if (y < h() - 1 && Math.abs(seam[y] - seam[y + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
                if (seam[y] == x) toggle = true;
                if (toggle) {
                    retargetedPicture.set(x, y, inputPicture.get(x + 1, y));
                    if (isTransposed) newEnergies[x][y] = energies[x + 1][y];
                    else newEnergies[y][x] = energies[y][x + 1];
                }
                else {
                    retargetedPicture.set(x, y, inputPicture.get(x, y));
                    if (isTransposed) newEnergies[x][y] = energies[x][y];
                    else newEnergies[y][x] = energies[y][x];
                }

            }
        }

        if (isTransposed) {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] == height() - 1) newEnergies[seam[i] - 1][i] = 1000;
                else newEnergies[seam[i]][i] = calculateEnergy(seam[i], i);
            }
        }
        else {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] == width() - 1) newEnergies[i][seam[i] - 1] = 1000;
                else newEnergies[i][seam[i]] = calculateEnergy(seam[i], i);
            }
        }
        // update the energy array
        /* double[][] newEnergies = new double[h()][w() - 1];
        for (int i = 0; i < h(); i++) {
            System.arraycopy(energies[i], 0, newEnergies[i], 0, seam[i]);
            System.arraycopy(energies[i], seam[i] + 1, newEnergies[i], seam[i],
                             w() - seam[i] - 1);
            // StdOut.print(i + " -- " + seam[i] + " -- " + width() + "\n");
            if (seam[i] == w() - 1) {
                newEnergies[i][seam[i] - 1] = 1000;
            }
            else {
                newEnergies[i][seam[i]] = calculateEnergy(seam[i], i);
            }

        } */
        inputPicture = retargetedPicture;
        energies = newEnergies;
    }
}
