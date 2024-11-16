package GBTAN;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Handles the physics simulation for the game, including ball movement, collision detection,
 * and interactions with objects such as blocks and boons.
 */
public class PhysicsEngine {
    /**
     * The game instance associated with this physics engine.
     */
    private final Game game;

    /**
     * The game data instance used for retrieving and managing game state.
     */
    private final GameData gameData;

    /**
     * The timer responsible for driving the physics loop at regular intervals.
     */
    private Timer physicsTimer;

    /**
     * The number of physics steps executed per timer tick, allowing finer control over the physics simulation.
     */
    private int physicsStepsPerTick;

    /**
     * Constructs a PhysicsEngine for the specified game.
     *
     * @param game The game instance for which the physics engine is responsible.
     */
    public PhysicsEngine(Game game) {
        this.game = game;
        this.gameData = game.getGameData();
        // The fastest a timer can tick in swing is 1ms, we may want to calculate physics more often
        // than that. We use a timer that's slower, and run physics more than once per tick.
        physicsTimer = new Timer(1000/GameSettings.PHYSICS_TIMER_FREQ, this::updatePhysics);
        physicsStepsPerTick = GameSettings.PHYSICS_FREQ * physicsTimer.getDelay()/1000;
        physicsTimer.start();
    }

    /**
     * Doubles the speed of the physics simulation without affecting the frame rate.
     */
    public void doubleSpeed() {
        physicsStepsPerTick *= 2;
    }

    /**
     * Resets the physics speed to its default value based on the game settings.
     */
    public void resetSpeed() {
        physicsStepsPerTick = GameSettings.PHYSICS_FREQ * physicsTimer.getDelay() / 1000;
    }

    /**
     * Retrieves the timer responsible for the physics simulation.
     *
     * @return The physics timer.
     */
    public Timer getPhysicsTimer() {
        return physicsTimer;
    }

    /**
     * Stops the physics simulation by halting the physics timer.
     */
    public void stopPhysics() {
        physicsTimer.stop();
    }

    /**
     * Starts the physics simulation by resuming the physics timer.
     */
    public void startPhysics() {
        physicsTimer.start();
    }

    /**
     * Updates the physics for all balls currently in play.
     *
     * @param event The event triggered by the physics timer.
     */
    private void updatePhysics(ActionEvent event) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (int i = 0; i < physicsStepsPerTick; i++) {
            for (Ball ball : ballsInPlay) {
                updateBall(ball);
            }
        }
    }

    /**
     * Calculates the intersection point of a line (defined by an angle and starting point) with a line segment.
     *
     * @param angle   The angle of the line in degrees.
     * @param p0      The starting point of the line.
     * @param segment The line segment to check for intersection.
     * @return The intersection point, or {@code null} if no intersection exists.
     */
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

    /**
     * Checks if a point lies on a given line segment, by checking if the sum of distances from the ends of
     * the segment add up to the length of the segment.
     *
     * @param p         The point to check.
     * @param lineStart The start of the line segment.
     * @param lineEnd   The end of the line segment.
     * @return {@code true} if the point lies on the segment; {@code false} otherwise.
     */
    private static boolean pointOnLine(Point2D.Double p, Point2D.Double lineStart, Point2D.Double lineEnd) {
        // A point is on a line if the sum of distances from the end points is roughly
        // equal to the length of the line.
        double l = lineEnd.distance(lineStart);
        double d1 = p.distance(lineStart);
        double d2 = p.distance(lineEnd);
        return Math.abs(l-d1-d2) < GameSettings.EPS;
    }

    /**
     * Determines the closest collision point between a ball and a block.
     *
     * @param ball  The ball to check for collision.
     * @param block The block to check for collision.
     * @return The closest collision point, or {@code null} if no collision exists.
     */
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

    /**
     * Identifies the FIRST collision point between a ball and any block in the game.
     *
     * @param ball The ball to check for collisions.
     * @return An entry containing the block and collision point, or {@code null} if no collisions are found.
     */
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

    /**
     * Updates the movement and state of a ball, handling collisions and interactions with game objects. Each round
     * we leave the ball in a state where it doesn't collided with anything. This way, at the start of a new round,
     * we can be sure that the ball is in a valid state, and only after it moves is it possible that there is a collision.
     *
     * @param ball The ball to update.
     */
    public void updateBall(Ball ball) {
        ball.move();  // since we moved, we may now have a collision on our hands, or we may have left the panel
        if (ball.getPosition().y > GameSettings.GAME_HEIGHT + ball.getRadius()) {  // Ball exited the play area
            gameData.getCannon().returnBall(ball);
            return;
        }
        for (Boon boon: gameData.getBoons()) {
            if (ball.getPosition().distance(boon.getPosition()) <= boon.getRadius() + ball.getRadius() + GameSettings.EPS) {
                boon.affect(ball);
            }
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
