import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int size;
    private treeNode root;

    private static class treeNode {

        private final Point2D point;
        private final RectHV r;
        private treeNode left;
        private treeNode right; // right is also up

        public treeNode(Point2D p, RectHV r) {
            this.point = p;
            this.r = r;
        }
    }

   
    public KdTree() {
        size = 0;
        root = null;
    }

  
    public boolean isEmpty() {
        return size == 0;
    }

   
    public int size() {
        return size;
    }

    public void insert(Point2D p) {

        root = insert(root, p, 0.0, 0.0, 1.0, 1.0, true);
    }

    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    public void draw() {
        draw(root, true);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<>();
        range(root, rect, q);
        return q;
    }

    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        }
        return nearest(root, p, root.point, true);
    }

    private treeNode insert(treeNode node, Point2D p, double x0, double y0,
            double x1, double y1, boolean xcmp) {

        if (node == null) {
            size++;
            RectHV r = new RectHV(x0, y0, x1, y1);
            return new treeNode(p, r);
        } else if (node.point.x() == p.x() && node.point.y() == p.y()) {
            return node;
        }

        if (xcmp) {
            double cmp = p.x() - node.point.x();
            if (cmp < 0) {
                node.left = insert(node.left, p, x0, y0, node.point.x(), y1, !xcmp);
            } else {
                node.right = insert(node.right, p, node.point.x(), y0, x1, y1, !xcmp);
            }
        } else {
            double cmp = p.y() - node.point.y();
            if (cmp < 0) {
                node.left = insert(node.left, p, x0, y0, x1, node.point.y(), !xcmp);
            } else {
                node.right = insert(node.right, p, x0, node.point.y(), x1, y1, !xcmp);
            }
        }
        return node;
    }

    private boolean contains(treeNode node, Point2D p, boolean xcmp) {

        if (node == null) {
            return false;
        } else if (node.point.x() == p.x() && node.point.y() == p.y()) {
            return true;
        } else {

            if (xcmp) {
                double cmp = p.x() - node.point.x();
                if (cmp < 0) {
                    return contains(node.left, p, !xcmp);
                } else {
                    return contains(node.right, p, !xcmp);
                }
            } else {
                double cmp = p.y() - node.point.y();
                if (cmp < 0) {
                    return contains(node.left, p, !xcmp);
                } else {
                    return contains(node.right, p, !xcmp);
                }
            }
        }
    }

    private void draw(treeNode node, boolean drawVert) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        if (drawVert) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.r.ymin(), node.point.x(), node.r.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.r.xmin(), node.point.y(), node.r.xmax(), node.point.y());
        }

        draw(node.left, !drawVert);
        draw(node.right, !drawVert);
    }

    private void range(treeNode node, RectHV rect, Queue<Point2D> q) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.point)) {
            q.enqueue(node.point);
        }

        if (rect.intersects(node.r)) {
            range(node.left, rect, q);
            range(node.right, rect, q);
        }
    }

    private Point2D nearest(treeNode node, Point2D p, Point2D c, boolean xcmp) {
        Point2D nearest = c;
        if (node == null) {
            return nearest;
        }
        if (node.point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = node.point;
        }
        if (node.r.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            treeNode near;
            treeNode far;
            if ((xcmp && (p.x() < node.point.x())) || (!xcmp && (p.y() < node.point.y()))) {
                near = node.left;
                far = node.right;
            } else {
                near = node.right;
                far = node.left;
            }
            nearest = nearest(near, p, nearest, !xcmp);
            nearest = nearest(far, p, nearest, !xcmp);
        }
        return nearest;
    }
}

