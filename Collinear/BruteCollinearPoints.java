/* *****************************************************************************
 *  Name: Kathleen
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] lines;
    private int numSegments;

    public BruteCollinearPoints(Point[] points) {

        if (points == null) throw new java.lang.IllegalArgumentException("Null argument");
        lines = new LineSegment[points.length * points.length];
        numSegments = 0;

        for (int i = 0; i <= points.length - 4; i++) {
            if (points[i] == null) throw new java.lang.IllegalArgumentException("Null point");
            Point smallest = points[i];
            Point largest = points[i];

            for (int j = i + 1; j <= points.length - 3; j++) {
                if (points[j].compareTo(smallest) < 0) smallest = points[j];
                else if (points[j].compareTo(largest) > 0) largest = points[j];

                for (int k = j + 1; k <= points.length - 2; k++) {
                    if (points[k].compareTo(smallest) < 0) smallest = points[k];
                    else if (points[k].compareTo(largest) > 0) largest = points[k];

                    for (int m = k + 1; m <= points.length - 1; m++) {
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[i].slopeTo(points[k]);

                        if (slope1 == Double.NEGATIVE_INFINITY
                                || slope2 == Double.NEGATIVE_INFINITY) {
                            throw new java.lang.IllegalArgumentException("Repeated point");
                        }

                        if (slope1 == slope2) {
                            double slope3 = points[i].slopeTo(points[m]);
                            if (slope3 == Double.NEGATIVE_INFINITY) {
                                throw new java.lang.IllegalArgumentException("Repeated point");
                            }
                            if (slope2 == slope3) {
                                if (points[m].compareTo(smallest) < 0) smallest = points[m];
                                else if (points[m].compareTo(largest) > 0) largest = points[m];

                                LineSegment line = new LineSegment(smallest, largest);
                                lines[numSegments++] = line;
                            }
                        }
                        // if the first two slopes aren't equal, just move to next iteration
                    }
                }
            }
        }
    }

    public int numberOfSegments() {

        return this.numSegments;
    }

    public LineSegment[] segments() {

        return Arrays.copyOf(this.lines, numSegments);
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
