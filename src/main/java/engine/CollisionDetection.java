package engine;

import model.*;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class CollisionDetection {
    public static double eps = 0.1;

    private static boolean pointOnLine(Point2D.Double p, Point2D.Double lineStart, Point2D.Double lineEnd) {
        // A point is on a line if the sum of distances from the end points is roughly
        // equal to the length of the line.
        double l = lineEnd.distance(lineStart);
        double d1 = p.distance(lineStart);
        double d2 = p.distance(lineEnd);
        return Math.abs(l-d1-d2) < eps;
    }

    public static Point2D.Double ballBlockCollisionPoint(Ball ball, Block block) {
        // This method checks for collision points, which can be:
        // - A corner
        // - A point along an edge
        // We may find several collision points (for example if the ball's circle
        // intersects with two edges and a corner). The resulting collision point
        // is whichever is closest to the ball's center.
        // Collision between an edge and the ball is according to this:
        // https://www.jeffreythompson.org/collision-detection/line-circle.php
        double effectiveRadius = ball.getRadius()+CollisionDetection.eps;
        Point2D.Double c = ball.getPosition();
        Point2D.Double[][] sides = block.getSides();
        Set<Point2D.Double> collisionPoints = new HashSet<>();
        for (Point2D.Double[] side: sides) {
            Point2D.Double p1 = side[0];
            Point2D.Double p2 = side[1];
            if (p1.distance(c) < effectiveRadius) {
                collisionPoints.add(p1);
            }
            if (p2.distance(c) < effectiveRadius) {
                collisionPoints.add(p2);
            }
            double cx = c.x;
            double cy = c.y;
            double x1 = p1.x;
            double x2 = p2.x;
            double y1 = p1.y;
            double y2 = p2.y;
            double l = p1.distance(p2);
            double dot = (((cx-x1)*(x2-x1))+((cy-y1)*(y2-y1)))/(l*l);
            double closestX = x1 + dot*(x2-x1);
            double closestY = y1 + dot*(y2-y1);
            Point2D.Double closest = new Point2D.Double(closestX, closestY);
            if (pointOnLine(closest, p1, p2) && ball.getPosition().distance(closest) <= effectiveRadius) {
                collisionPoints.add(closest);
            }
        }
        if (!collisionPoints.isEmpty()) {
            return Collections.min(collisionPoints, (p1, p2) -> {
                Point2D.Double p0 = ball.getPosition();
                return Double.compare(p0.distance(p1), p0.distance(p2));
            });
        } else {
            return null;
        }
    }
}
