package GBTAN;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

public class PhysicsEngine {
    private final Game game;
    private final GameData gameData;
    private javax.swing.Timer physicsTimer;
    private int physicsStepsPerTick;

    public PhysicsEngine(Game game) {
        this.game = game;
        this.gameData = game.getGameData();
        // The fastest a timer can tick in swing is 1ms, we may want to calculate physics more often
        // than that. We use a timer that's slower, and run physics more than once per tick.
        physicsTimer = new Timer(1000/GameSettings.PHYSICS_TIMER_FREQ, this::updatePhysics);
        physicsStepsPerTick = GameSettings.PHYSICS_FREQ * physicsTimer.getDelay()/1000;
        physicsTimer.start();
    }

    public Timer getPhysicsTimer() {
        return physicsTimer;
    }

    public void stopPhysics() {
        physicsTimer.stop();
    }

    public void startPhysics() {
        physicsTimer.start();
    }

    private void updatePhysics(ActionEvent event) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (int i=0; i<physicsStepsPerTick; i++) {
            for (Ball ball: ballsInPlay) {
                updateBall(ball);
            }
        }
    }

    private static Point2D.Double lineAndSegment(double angle, Point2D.Double p0, Line2D.Double segment) {
        //https://www.jeffreythompson.org/collision-detection/line-line.php
        angle = -Math.toRadians(angle);
        Point2D.Double p1 = new Point2D.Double(segment.x1, segment.y1);
        Point2D.Double p2 = new Point2D.Double(segment.x2, segment.y2);
        double dx = Math.cos(angle);
        double dy = Math.sin(angle);
        double segmentDx = p2.x - p1.x;
        double segmentDy = p2.y - p1.y;
        double denominator = dx * segmentDy - dy * segmentDx;
        if (Math.abs(denominator) <= GameSettings.EPS) {  // the lines are parallel
            return null;
        }
        // p0 + t * (dx, dy)
        double t = ((p1.x - p0.x) * segmentDy - (p1.y - p0.y) * segmentDx) / denominator;
        // p1 + u * (segmentDx, segmentDy)
        double u = ((p1.x - p0.x) * dy - (p1.y - p0.y) * dx) / denominator;
        if (t >= 0 && u >= 0 && u <= 1) {
            return new Point2D.Double(p0.x + t * dx, p0.y + t * dy);
        }
        return null;
    }

    private static boolean pointOnLine(Point2D.Double p, Point2D.Double lineStart, Point2D.Double lineEnd) {
        // A point is on a line if the sum of distances from the end points is roughly
        // equal to the length of the line.
        double l = lineEnd.distance(lineStart);
        double d1 = p.distance(lineStart);
        double d2 = p.distance(lineEnd);
        return Math.abs(l-d1-d2) < GameSettings.EPS;
    }

    private static Point2D.Double ballBlockCollisionPoint(Ball ball, Block block) {
        // This method checks for collision points, which can be:
        // - A corner
        // - A point along an edge
        // We may find several collision points (for example if the ball's circle
        // intersects with two edges and a corner). The resulting collision point
        // is whichever is closest to the ball's center.
        // Collision between an edge and the ball is according to this:
        // https://www.jeffreythompson.org/collision-detection/line-circle.php
        Point2D.Double c = ball.getPosition();
        Line2D.Double[] sides = block.getSides();
        Set<Point2D.Double> collisionPoints = new HashSet<>();
        // We calculate collisions for each side of the Block (polygon)
        for (Line2D.Double side: sides) {
            Point2D.Double p1 = new Point2D.Double(side.x1, side.y1);
            Point2D.Double p2 = new Point2D.Double(side.x2, side.y2);
            if (p1.distance(c) < ball.getRadius()) {  // they collide in the 1st corner
                collisionPoints.add(p1);
            }
            if (p2.distance(c) < ball.getRadius()) {  // they collide in the 2nd corner
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
            Point2D.Double closest = new Point2D.Double(x1 + dot*(x2-x1), y1 + dot*(y2-y1));
            if (pointOnLine(closest, p1, p2) && ball.getPosition().distance(closest) <= ball.getRadius()) {
                collisionPoints.add(closest);
            }
        }
        // We might have several collision points by now. Use the one that's closest to the ball's center.
        if (!collisionPoints.isEmpty()) {
            return Collections.min(collisionPoints, (p1, p2) -> {
                Point2D.Double p0 = ball.getPosition();
                return Double.compare(p0.distance(p1), p0.distance(p2));
            });
        } else {
            return null;
        }
    }

    private void returnBallToCannon(Ball ball) {
        if (gameData.getBallsReturned().isEmpty()) {
            gameData.getCannon().setPosition(ball.getPosition().x);
        }
        ball.setState(Ball.BallState.RETURNED);
    }

    public AbstractMap.SimpleEntry<Block, Point2D.Double> firstCollisionPoint(Ball ball) {
        // This method checks for collisions among all the blocks within minimum collision distance,
        // and returns immediately upon finding a colliding block. Its return is the block
        // with which the ball collides, and the point where they collide (like a tuple in python).
        // If no blocks collide, return null.
        double minCollisionDistance = ball.getRadius() + GameSettings.BLOCK_WIDTH/Math.sqrt(2) + GameSettings.EPS;
        for (Block block: gameData.getBlocks()) {
            if (ball.getPosition().distance(block.getPosition()) <= minCollisionDistance) {
                Point2D.Double collisionPoint = ballBlockCollisionPoint(ball, block);
                if (collisionPoint != null) {
                    return new AbstractMap.SimpleEntry<>(block, collisionPoint);
                }
            }
        }
        return null;
    }

    public void updateBall(Ball ball) {
        // Each round we leave the ball in a state where it doesn't collide with anything. This way,
        // at the start of a new round, we don't need to check for collisions, only after moving.
        ball.move();  // since we moved, we may now have a collision on our hands, or we may have left the panel
        if (ball.getPosition().y > GameSettings.GAME_HEIGHT + ball.getRadius()) {  // Ball exited the play area
            returnBallToCannon(ball);
            return;
        }
        ball.bounceOffWalls();
        AbstractMap.SimpleEntry<Block, Point2D.Double> blockAndPoint = firstCollisionPoint(ball);
        while (blockAndPoint !=  null) { // while any collisions are left, handle them 1 by 1
            Block block = blockAndPoint.getKey();
            Point2D.Double collisionPoint = blockAndPoint.getValue();
            ball.bounceOffPoint(collisionPoint); // reflect off a block
            block.decrementHealth(); // decrement HP, and remove if necessary
            // then move away from the block until the collision is resolved
            while (ballBlockCollisionPoint(ball, block) != null) {
                ball.move();
            }
            // then move on to the next colliding block
            blockAndPoint = firstCollisionPoint(ball);
        }

    }
}
