/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final boolean VERT = true;
    private static final boolean HORIZ = false;
    private Node root;
    private int size;

    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private boolean direction;
        private RectHV leftRect;
        private RectHV rightRect;

        public Node(Point2D p, boolean direction, RectHV parentRect) {

            this.point = p;
            this.left = null;
            this.right = null;
            this.direction = direction;

            // if line is vertical
            if (this.direction == VERT) {
                this.leftRect = new RectHV(parentRect.xmin(),
                                           parentRect.ymin(),
                                           p.x(),
                                           parentRect.ymax());
                this.rightRect = new RectHV(p.x(),
                                            parentRect.ymin(),
                                            parentRect.xmax(),
                                            parentRect.ymax());

            }
            else {
                this.leftRect = new RectHV(parentRect.xmin(),
                                           parentRect.ymin(),
                                           parentRect.xmax(),
                                           p.y());
                this.rightRect = new RectHV(parentRect.xmin(),
                                            p.y(),
                                            parentRect.xmax(),
                                            parentRect.ymax());
            }

        }
    }

    public KdTree() {

        root = null;
        size = 0;
    }

    public boolean isEmpty() {

        return (this.size == 0);
    }

    public int size() {

        return this.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException("null point");

        RectHV board = new RectHV(0.0, 0.0, 1.0, 1.0);

        root = insert(root, p, VERT, board);
    }

    private Node insert(Node node, Point2D p, boolean direc, RectHV parentRect) {

        if (node == null) {
            this.size++;
            return new Node(p, direc, parentRect);
        }
        // if line should be is vertical, x coor is key
        if (direc) {
            if (p.x() < node.point.x()) {
                if (node.leftRect.contains(p)) {
                    node.left = insert(node.left, p, !node.direction, node.leftRect);
                }
                else node.left = insert(node.left, p, !node.direction, node.rightRect);
            }
            else if (p.x() != node.point.x() || p.y() != node.point.y()) {
                if (node.rightRect.contains(p)) {
                    node.right = insert(node.right, p, !node.direction, node.rightRect);
                }
                else node.right = insert(node.right, p, !node.direction, node.leftRect);
            }
        }
        else {
            if (p.y() < node.point.y()) {
                if (node.leftRect.contains(p)) {
                    node.left = insert(node.left, p, !node.direction, node.leftRect);
                }
                else node.left = insert(node.left, p, !node.direction, node.rightRect);
            }
            else if (p.x() != node.point.x() || p.y() != node.point.y()) {
                if (node.rightRect.contains(p)) {
                    node.right = insert(node.right, p, !node.direction, node.rightRect);
                }
                else node.right = insert(node.right, p, !node.direction, node.leftRect);
            }
        }

        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException("null point");

        Node node = root;
        while (node != null) {
            if (node.direction == VERT) {
                if (p.x() < node.point.x()) {
                    if (node.left == null) return false;
                    node = node.left;
                }
                else if (p.x() == node.point.x() && p.y() == node.point.y()) return true;
                else {
                    if (node.right == null) return false;
                    node = node.right;
                }
            }
            else {
                if (p.y() < node.point.y()) {
                    if (node.left == null) return false;
                    node = node.left;
                }
                else if (p.x() == node.point.x() && p.y() == node.point.y()) return true;
                else {
                    if (node.right == null) return false;
                    node = node.right;
                }
            }
        }
        return false;
    }

    public void draw() {

        Node currNode = this.root;

        draw(currNode);

    }

    private void draw(Node currNode) {

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(currNode.point.x(), currNode.point.y());
        if (currNode.direction == VERT) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(currNode.point.x(),
                         currNode.leftRect.ymin(),
                         currNode.point.x(),
                         currNode.leftRect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(currNode.leftRect.xmin(),
                         currNode.point.y(),
                         currNode.leftRect.xmax(),
                         currNode.point.y());
        }

        if (currNode.left != null) draw(currNode.left);
        if (currNode.right != null) draw(currNode.right);
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) throw new java.lang.IllegalArgumentException("null argument");

        Queue<Point2D> points = new Queue<Point2D>();

        if (root != null) {
            Node location = root;
            range(rect, location, points);
        }
        return points;
    }

    private void range(RectHV rect, Node location, Queue<Point2D> points) {
        if (rect.contains(location.point)) {
            points.enqueue(location.point);
        }

        if (location.left != null && location.leftRect.intersects(rect)) {
            range(rect, location.left, points);
        }
        if (location.right != null && location.rightRect.intersects(rect)) {
            range(rect, location.right, points);
        }
    }

    public Point2D nearest(Point2D p) {

        if (p == null) throw new java.lang.IllegalArgumentException("null point");

        if (this.root == null) return null;

        if (this.contains(p)) return p;

        Point2D nearestPt = this.root.point;
        double nearestDist = p.distanceSquaredTo(nearestPt);

        nearestPt = nearest(p, this.root, nearestPt, nearestDist);

        return nearestPt;
    }

    private Point2D nearest(Point2D p, Node currNode, Point2D nearestPt, double nearestDist) {

        double currDist = currNode.point.distanceSquaredTo(p);
        if (currDist < nearestDist) {
            nearestDist = currDist;
            nearestPt = currNode.point;
        }
        

        double leftDist = currNode.leftRect.distanceSquaredTo(p);
        double rightDist = currNode.rightRect.distanceSquaredTo(p);

        if (leftDist < rightDist) {
            if (leftDist < nearestDist && currNode.left != null) {
                nearestPt = nearest(p, currNode.left, nearestPt, nearestDist);
                nearestDist = nearestPt.distanceSquaredTo(p);
            }
            if (rightDist < nearestDist && currNode.right != null) {
                nearestPt = nearest(p, currNode.right, nearestPt, nearestDist);
                // nearestDist = nearestPt.distanceSquaredTo(p);
            }
        }
        else {
            if (rightDist < nearestDist && currNode.right != null) {
                nearestPt = nearest(p, currNode.right, nearestPt, nearestDist);
                nearestDist = nearestPt.distanceSquaredTo(p);
            }
            if (leftDist < nearestDist && currNode.left != null) {
                nearestPt = nearest(p, currNode.left, nearestPt, nearestDist);
                // nearestDist = nearestPt.distanceSquaredTo(p);
            }
        }

        return nearestPt;
    }


    public static void main(String[] args) {

        KdTree tree = new KdTree();
        Point2D pa = new Point2D(0.206107, 0.095492);
        tree.insert(pa);
        Point2D pb = new Point2D(0.975528, 0.654508);
        tree.insert(pb);
        Point2D pc = new Point2D(0.024472, 0.345492);
        tree.insert(pc);
        Point2D pd = new Point2D(0.793893, 0.095492);
        tree.insert(pd);
        Point2D pe = new Point2D(0.024472, 0.345492);
        tree.insert(pe);
        Point2D pf = new Point2D(0.975528, 0.345492);
        tree.insert(pf);
        Point2D pg = new Point2D(0.206107, 0.904508);
        tree.insert(pg);
        Point2D ph = new Point2D(0.500000, 0.000000);
        tree.insert(ph);
        Point2D pi = new Point2D(0.024472, 0.654508);
        tree.insert(pi);
        Point2D pj = new Point2D(0.500000, 1.000000);
        tree.insert(pj);


        Point2D pp = new Point2D(0.75, 0.75);
        System.out.println(tree.contains(pp));
        System.out.println("nearest: " + tree.nearest(pp));
    }
}
