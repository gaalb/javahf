package engine;

import model.*;

import java.awt.*;
import java.awt.geom.Point2D;


public class CollisionDetection {
    public static double eps = 0.1;

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
        double effectiveRadius = ball.getRadius() + CollisionDetection.eps;
        for (Point2D.Double corner: block.getCorners()) {
            if (corner.distance(ball.getPosition()) <= effectiveRadius) {
                return corner;
            }
        }
        Point2D.Double[][] sides = block.getSides();
        // https://www.jeffreythompson.org/collision-detection/line-circle.php
        for (Point2D.Double[] side: sides) {
            Point2D.Double p1 = side[0];
            Point2D.Double p2 = side[1];
            double cx = ball.getPosition().x;
            double cy = ball.getPosition().y;
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
                return closest;
            }
        }
        return null;
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
