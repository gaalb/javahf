package v1;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.Point;

public class Ball {
    private Point2D.Double position;
    private Point2D.Double velocity;
    private boolean active;

    private static final int SIZE = 10; // Ball size

    public Ball(Point startPosition, double angle) {
        this.position = new Point2D.Double(startPosition.x, startPosition.y);
        double speed = 5.0; // Ball speed
        this.velocity = new Point2D.Double(
                speed * Math.cos(Math.toRadians(angle)),
                -speed * Math.sin(Math.toRadians(angle))
        );
        this.active = true;
    }

    public void update() {
        position.x += velocity.x;
        position.y += velocity.y;
        // Check for wall collisions
        System.out.println("CCAWCAWCACWA");
        checkWallCollisions();
    }

    private void checkWallCollisions() {
        // Left and right walls
        if (position.x <= 0 || position.x >= GamePanel.SCREEN_SIZE.width - SIZE) {
            velocity.x = -velocity.x;
        }
        // Top wall
        if (position.y <= 0) {
            velocity.y = -velocity.y;
        }
        // Bottom wall (return to player)
        if (position.y >= GamePanel.SCREEN_SIZE.height - SIZE) {
            active = false;
            // Optionally handle ball return logic here
        }
    }

    public void bounce() {
        velocity.y = -velocity.y;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) position.x, (int) position.y, SIZE, SIZE);
    }

    public boolean isActive() {
        return active;
    }

    public Point2D.Double getPosition() {
        return position;
    }
}
