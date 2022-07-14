

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

/**
 *
 * @author ghaith
 */
public class PointSET {

    private final SET<Point2D> pointSet;

    public PointSET() {      // construct an empty set of points 
        pointSet = new SET<>();
    }

    public boolean isEmpty() {      // is the set empty? 
        return pointSet.isEmpty();
    }

    public int size() {     // number of points in the set 
        return pointSet.size();
    }

    public void insert(Point2D p) {     // add the point to the set (if it is not already in the set)
        if (p != null) {
            pointSet.add(p);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean contains(Point2D p) {    // does the set contain point p? 
        if (p != null) {

            return pointSet.contains(p);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void draw() {    // draw all points to standard draw 
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect != null) {
            Queue<Point2D> q = new Queue<>();
            for (Point2D p : pointSet) {
                if (rect.contains(p)) {
                    q.enqueue(p);
                }
            }
            return q;
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a nearest neighbor in the set to p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p != null) {
            Point2D nearest = null;
            for (Point2D pIter : pointSet) {
                if (nearest == null
                        || pIter.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                    nearest = pIter;
                }
            }
            return nearest;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /*  public static void main(String[] args) {// unit testing of the methods (optional) 
    }*/
}

