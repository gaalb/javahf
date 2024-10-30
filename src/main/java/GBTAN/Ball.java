package GBTAN;

import java.awt.geom.Point2D;

public class Ball {
    public enum BallState {
        IN_STORE, IN_PLAY, RETURNED;
    }

    private Point2D.Double position;
    private Point2D.Double velocity;
    private double radius;
    private BallState state;
    private final Game game;

    public Ball(Point2D.Double position, Point2D.Double velocity, double radius, BallState state, Game game) {
        this.game = game;
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.state = state;
    }

    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y); // clone to avoid accidents
    }

    public Point2D.Double getVelocity() {
        return new Point2D.Double(velocity.x, velocity.y);
    }

    public double getRadius() {
        return radius;
    }

    public void setPosition(Point2D.Double position) {
        this.position = new Point2D.Double(position.x, position.y);
    }

    public void setPosition(double x, double y) {
        this.position = new Point2D.Double(x, y);
    }

    public void setVelocity(Point2D.Double velocity) {
        this.velocity = new Point2D.Double(velocity.x, velocity.y);
    }

    public void setVelocity(double x, double y) {
        this.velocity = new Point2D.Double(x, y);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setState(BallState state) {
        this.state = state;
    }

    public BallState getState() {
        return state;
    }

    public void move() {
        position.x += velocity.x;;
        position.y += velocity.y;
    }

    public void bounceOffWalls() {
        if (position.x < radius) velocity.x = Math.abs(velocity.x); // left wall
        if (position.x > GameSettings.GAME_WIDTH - radius) velocity.x = -Math.abs(velocity.x);  // right wall
        if (position.y < radius) velocity.y = Math.abs(velocity.y); // top wall
    }

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
