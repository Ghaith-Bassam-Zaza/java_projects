import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;
import java.util.NoSuchElementException;

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

    public Iterable<Point2D> range(RectHV rect) {    // all points that are inside the rectangle (or on the boundary) 
        ArrayList<Point2D> recPoints = new ArrayList<>();
        if (rect != null) {
            Point2D lowest = new Point2D(rect.xmin(), rect.ymax());
            while (true) {
                try {
                    lowest = pointSet.floor(lowest);
                    if (lowest.y() > rect.ymin() && lowest.x() > rect.xmin() && lowest.x() < rect.xmax()) {
                        recPoints.add(lowest);

                    } else if (lowest.y() < rect.ymin()) {
                        break;
                    }
                } catch (NoSuchElementException e) {
                    break;
                }
            }
            return recPoints;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Point2D nearest(Point2D p) {     // a nearest neighbor in the set to point p; null if the set is empty 
        if (p != null) {
            if (isEmpty()) {
                return null;
            }
            Point2D lowerWinner = null;
            Point2D higherWinner = null;
            Point2D lowest;
            Point2D highest;
            try {
                do {
                    if (lowerWinner != null) {
                        lowest = pointSet.floor(lowerWinner);
                        if (lowerWinner.distanceTo(p) > lowest.y()) {
                            lowerWinner = lowest;
                        }
                    } else {
                        lowerWinner = pointSet.floor(p);
                        lowest = lowerWinner;
                    }
                } while (lowerWinner.distanceTo(p) > Math.abs(lowest.y() - p.y()));
            } catch (NoSuchElementException e) {

            }
            try {
                do {
                    if (higherWinner != null) {
                        highest = pointSet.ceiling(higherWinner);
                        if (higherWinner.distanceTo(p) > highest.y()) {
                            higherWinner = highest;
                        }
                    } else {
                        higherWinner = pointSet.ceiling(p);
                        highest = higherWinner;
                    }
                } while (higherWinner.distanceTo(p) > Math.abs(highest.y() - p.y()));

            } catch (NoSuchElementException e) {

            }
            if (lowerWinner.distanceTo(p) < higherWinner.distanceTo(p)) {
                return lowerWinner;
            } else {
                return higherWinner;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /*  public static void main(String[] args) {// unit testing of the methods (optional) 
    }*/
}

