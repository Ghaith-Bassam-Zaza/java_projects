

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ghaith
 */
public class BruteCollinearPoints {

    private int n;
    private ArrayList<LineSegment> ls;
    private Point[] previousLine;

    public BruteCollinearPoints(Point[] points) {
        previousLine = new Point[2];
        if (points != null) {

            ls = new ArrayList<>();
            n = 0;
            if (points.length < 4) {
                for (int i = 0; i < points.length; i++) {
                    if (points[i] == null) {
                        throw new IllegalArgumentException();
                    }
                    for (int j = 0; j < i; j++) {
                        if (points[i].compareTo(points[j]) == 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
            for (int i = 0; i < points.length - 3; i++) {
                if (points[i] != null) {
                    for (int j = i + 1; j < points.length - 2; j++) {
                        if (points[j] != null && points[i].compareTo(points[j]) != 0) {
                            double s1 = points[i].slopeTo(points[j]);
                            for (int k = j + 1; k < points.length - 1; k++) {

                                if (points[k] != null && points[i].compareTo(points[k]) != 0 && points[j].compareTo(points[k]) != 0) {
                                    double s2 = points[j].slopeTo(points[k]);
                                    if (s1 == s2) {
                                        for (int l = k + 1; l < points.length; l++) {
                                            if (points[l] != null && points[i].compareTo(points[l]) != 0 && points[j].compareTo(points[l]) != 0 && points[k].compareTo(points[l]) != 0) {
                                                double s3 = points[k].slopeTo(points[l]);
                                                if (s2 == s3) {
                                                    n++;
                                                    Point[] line = new Point[4];
                                                    line[0] = points[i];
                                                    line[1] = points[j];
                                                    line[2] = points[k];
                                                    line[3] = points[l];
                                                    Arrays.sort(line);
                                                    if (previousLine[0] != null && (line[0].compareTo(previousLine[0]) == 0 && line[3].compareTo(previousLine[1]) == 0)) {
                                                        continue;
                                                    }

                                                    ls.add(new LineSegment(line[0], line[3]));

                                                    previousLine[0] = line[0];
                                                    previousLine[1] = line[3];

                                                }
                                            } else {
                                                throw new IllegalArgumentException();
                                            }
                                        }
                                    }
                                } else {
                                    throw new IllegalArgumentException();
                                }

                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int numberOfSegments() {
        return n;
    }

    public LineSegment[] segments() {
        int s = ls.size();
        LineSegment[] Arr = new LineSegment[s];
        for (int i = 0; i < s; i++) {
            Arr[i] = ls.get(i);
        }
        return Arr;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}

