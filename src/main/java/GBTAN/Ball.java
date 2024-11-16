package GBTAN;

import java.awt.geom.Point2D;

/**
 * Represents a ball in the game, which interacts with the game environment,
 * such as bouncing off walls and points.
 */
public class Ball {

    /**
     * Represents the state of the ball during gameplay.
     */
    public enum BallState {
        /**
         * The ball is in the cannon, ready to be fired.
         */
        IN_STORE,

        /**
         * The ball is flying around in the game area.
         */
        IN_PLAY,

        /**
         * The ball has exited the game area but is not yet ready to fire.
         */
        RETURNED
    }

    /**
     * The current position of the ball.
     */
    private Point2D.Double position;

    /**
     * The velocity of the ball, represented as a vector.
     */
    private Point2D.Double velocity;

    /**
     * The radius of the ball.
     */
    private double radius;

    /**
     * The current state of the ball.
     */
    private BallState state;

    /**
     * The game instance the ball belongs to.
     */
    private final Game game;

    /**
     * Creates a new Ball with the specified position, velocity, radius, state, and game reference.
     *
     * @param position the initial position of the ball.
     * @param velocity the initial velocity of the ball.
     * @param radius the radius of the ball.
     * @param state the initial state of the ball.
     * @param game the game instance the ball belongs to.
     */
    public Ball(Point2D.Double position, Point2D.Double velocity, double radius, BallState state, Game game) {
        this.game = game;
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.state = state;
    }

    /**
     * Gets the current position of the ball.
     *
     * @return a copy of the ball's position to ensure immutability.
     */
    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y);
    }

    /**
     * Gets the current velocity of the ball.
     *
     * @return a copy of the ball's velocity to ensure immutability.
     */
    public Point2D.Double getVelocity() {
        return new Point2D.Double(velocity.x, velocity.y);
    }

    /**
     * Calculates the absolute magnitude of the ball's velocity vector.
     *
     * @return the length of the velocity vector.
     */
    public double getAbsVelocity() {
        return Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2));
    }

    /**
     * Gets the radius of the ball.
     *
     * @return the radius of the ball.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the position of the ball.
     *
     * @param position the new position of the ball.
     */
    public void setPosition(Point2D.Double position) {
        this.position = new Point2D.Double(position.x, position.y);
    }

    /**
     * Sets the position of the ball.
     *
     * @param x the x-coordinate of the new position.
     * @param y the y-coordinate of the new position.
     */
    public void setPosition(double x, double y) {
        this.position = new Point2D.Double(x, y);
    }

    /**
     * Sets the velocity of the ball.
     *
     * @param velocity the new velocity vector of the ball.
     */
    public void setVelocity(Point2D.Double velocity) {
        this.velocity = new Point2D.Double(velocity.x, velocity.y);
    }

    /**
     * Sets the velocity of the ball.
     *
     * @param x the x-component of the velocity vector.
     * @param y the y-component of the velocity vector.
     */
    public void setVelocity(double x, double y) {
        this.velocity = new Point2D.Double(x, y);
    }

    /**
     * Sets the radius of the ball.
     *
     * @param radius the new radius of the ball.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Sets the state of the ball.
     *
     * @param state the new state of the ball.
     */
    public void setState(BallState state) {
        this.state = state;
    }

    /**
     * Gets the current state of the ball.
     *
     * @return the current state of the ball.
     */
    public BallState getState() {
        return state;
    }

    /**
     * Moves the ball based on its velocity.
     */
    public void move() {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    /**
     * Adjusts the ball's velocity to simulate a bounce off the walls.
     * The ball escapes through the bottom wall, so no adjustment is made for that.
     */
    public void bounceOffWalls() {
        if (position.x < radius) velocity.x = Math.abs(velocity.x); // left wall
        if (position.x > GameSettings.GAME_WIDTH - radius) velocity.x = -Math.abs(velocity.x);  // right wall
        if (position.y < radius) velocity.y = Math.abs(velocity.y); // top wall
    }

    /**
     * Adjusts the ball's velocity to simulate a bounce off a specific point.
     * The bounce only occurs if the ball is moving towards the point.
     *
     * @param point the point the ball is colliding with.
     */
    public void bounceOffPoint(Point2D.Double point) {
        // Calculates the portion of the ball's velocity which is parallel
        // to the line drawn between the ball's center and the collision
        // point, and inverts that component.
        // Only changes velocity direction, doesn't change position: that is the job for move()
        Point2D.Double direction = new Point2D.Double(point.x-position.x, point.y-position.y);
        double magnitudeDir = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        Point2D.Double unitDirection = new Point2D.Double(direction.x / magnitudeDir, direction.y / magnitudeDir);
        double dot = velocity.x * unitDirection.x + velocity.y * unitDirection.y;
        // If dot is negative, that means that the direction of the ball is away from the
        // intersecting/connecting point. This happens because of the inaccuracies in
        // the collision detection, and is not physically feasible. When this happens,
        // the bounce is ignored, and the direction isn't changed.
        if (dot > 0) {
            Point2D.Double projection = new Point2D.Double(dot * unitDirection.x, dot*unitDirection.y);
            Point2D.Double perpendicular = new Point2D.Double(velocity.x - projection.x, velocity.y-projection.y);
            velocity.x = perpendicular.x - projection.x;
            velocity.y = perpendicular.y - projection.y;
        }
    }
}
