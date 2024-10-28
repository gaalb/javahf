package engine;

import model.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class CollisionDetection {
    // https://www.jeffreythompson.org/collision-detection/line-circle.php
    public static double eps = 0.1;

    private static Point2D.Double averagePoint(List<Point2D.Double> points) {
        Point2D.Double avg = new Point2D.Double(0, 0);
        for (Point2D.Double point: points) {
            avg.x += point.x;
            avg.y += point.y;
        }
        avg.x /= points.size();
        avg.y /= points.size();
        return avg;
    }

    private static boolean pointOnLine(Point2D.Double p, Point2D.Double lineStart, Point2D.Double lineEnd) {
        double l = lineEnd.distance(lineStart);
        double d1 = p.distance(lineStart);
        double d2 = p.distance(lineEnd);
        System.out.printf("%.3f\n",Math.abs(l-d1-d2));
        boolean ret = Math.abs(l-d1-d2) < eps;
        if (ret) System.out.println("Point on line!");
        return ret;
    }

    public static Point2D.Double ballBlock(Ball ball, Block block) {
        double effectiveRadius = ball.getRadius();
        Point2D.Double c = ball.getPosition();

        Point2D.Double[][] sides = block.getSides();
        List<Point2D.Double> collisionPoints = new LinkedList<>();
        for (Point2D.Double[] side: sides) {
            Point2D.Double p1 = side[0];
            Point2D.Double p2 = side[1];
            if (p1.distance(c) < effectiveRadius) {
                collisionPoints.add(p1);
            } else if (p2.distance(c) < effectiveRadius) {
                collisionPoints.add(p2);
            } else {
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
        }
        if (!collisionPoints.isEmpty()) {
            return Collections.max(collisionPoints, (p1, p2) -> {
                Point2D.Double p0 = ball.getPosition();
                return Double.compare(p0.distance(p1), p0.distance(p2));
            });
        } else {
            return null;
        }
    }

    public static Point2D.Double reflectVelocity(Point2D.Double velocity, Point2D.Double direction) {
        // Normalize the direction vector
        double dirMagnitude = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        Point2D.Double unitDirection = new Point2D.Double(direction.x / dirMagnitude, direction.y / dirMagnitude);

        // Project the velocity onto the direction vector
        double dotProduct = velocity.x * unitDirection.x + velocity.y * unitDirection.y;
        Point2D.Double projection = new Point2D.Double(dotProduct * unitDirection.x, dotProduct * unitDirection.y);

        // Reverse the projection to get the reflection
        Point2D.Double reflectedProjection = new Point2D.Double(-projection.x, -projection.y);

        // Calculate the perpendicular component by subtracting the original projection from the velocity
        Point2D.Double perpendicularComponent = new Point2D.Double(velocity.x - projection.x, velocity.y - projection.y);

        // Add the perpendicular component and the reversed projection to get the modified velocity
        return new Point2D.Double(perpendicularComponent.x + reflectedProjection.x, perpendicularComponent.y + reflectedProjection.y);
    }
}
