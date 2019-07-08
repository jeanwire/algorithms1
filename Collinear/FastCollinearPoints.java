/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private LineSegment[] lines;
    private int numSegments;

    public FastCollinearPoints(Point[] points) {

        if (points == null) throw new java.lang.IllegalArgumentException("Null argument");

        lines = new LineSegment[points.length * points.length];
        numSegments = 0;

        for (int i = 0; i < points.length; i++) {

            if (points[i] == null) throw new java.lang.IllegalArgumentException("Null point");

            Point[] copy = Arrays.copyOf(points, points.length);
            // flip the point to the first space in the array
            Point temp = copy[0];
            copy[0] = copy[i];
            copy[i] = temp;

            Comparator<Point> BY_SLOPE = copy[0].slopeOrder();
            Arrays.sort(copy, 1, copy.length, BY_SLOPE);

            if (copy.length > 1) {
                if (copy[1].compareTo(copy[0]) == 0) {
                    throw new java.lang.IllegalArgumentException("Repeated point");
                }

                int counter = 1;
                for (int j = 1; j < copy.length - 1; j++) {

                    if (copy[0].slopeTo(copy[j]) == copy[0].slopeTo(copy[j + 1])) {
                        counter++;
                        if (counter == 3) {
                            Point smallest = copy[0];
                            Point largest = copy[0];
                            for (int k = j + 1; k > j - 2; k--) {
                                if (copy[k].compareTo(smallest) < 0) smallest = copy[k];
                                else if (copy[k].compareTo(largest) > 0) largest = copy[k];
                            }
                            LineSegment segment = new LineSegment(smallest, largest);
                            boolean duplicate = false;
                            for (int k = 0; k < numSegments; k++) {
                                if (segment.toString().equals(lines[k].toString())) {
                                    duplicate = true;
                                    break;
                                }
                            }
                            if (!duplicate) {
                                lines[numSegments++] = segment;
                            }
                            counter = 1;
                        }
                    }
                    else counter = 1;
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
