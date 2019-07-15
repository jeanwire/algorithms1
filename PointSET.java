/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;

public class PointSET {

    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return (this.set.size() == 0);
    }

    public int size() {
        return this.set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException("null point");

        this.set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException("null point");

        return this.set.contains(p);
    }

    public void draw() {

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        Iterator<Point2D> points;

        for (points = set.iterator(); points.hasNext(); ) {
            points.next().draw();
        }

        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {

        Stack<Point2D> points = new Stack<Point2D>();

        Iterator<Point2D> iter;

        for (iter = set.iterator(); iter.hasNext(); ) {
            Point2D point = iter.next();
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax()) {
                if (point.y() >= rect.ymin() && point.y() <= rect.ymax()) {
                    points.push(point);
                }
            }
        }

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException("null point");

        Iterator<Point2D> points;
        Stack<Point2D> pointStack = new Stack<Point2D>();
        double minDist = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;

        for (points = set.iterator(); points.hasNext(); ) {
            pointStack.push(points.next());
        }

        for (Point2D point : pointStack) {

            double distance = Math.pow((point.x() - p.x()), 2) + Math.pow((point.y() - p.y()), 2);
            if (distance < minDist) {
                minDist = distance;
                minPoint = point;
            }
        }

        return minPoint;
    }

    public static void main(String[] args) {

    }
}
