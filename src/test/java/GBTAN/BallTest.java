package GBTAN;
import GBTAN.Ball.BallState;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;

public class BallTest {
    /**
     * Test if a ball correctly bounces off a point.
     */
    @Test
    public void testPointBounce() {
        Point2D.Double pos = new Point2D.Double(100, 100);
        Point2D.Double vel = new Point2D.Double(1, 1);
        Ball ball = new Ball(pos, vel,GameSettings.BALL_RADIUS, BallState.IN_PLAY, null);
        // p is to the right of the ball -> reverse x direction
        Point2D.Double p = new Point2D.Double(100+GameSettings.BALL_RADIUS, 100);
        ball.bounceOffPoint(p);
        assertEquals(ball.getPosition(), pos);
        assertEquals(ball.getVelocity().y, 1, 0.0001);
        assertEquals(ball.getVelocity().x, -1, GameSettings.EPS);
    }

    /**
     * Test if a ball correctly bounces off walls.
     */
    @Test
    public void testWallBounce() {
        // The ball is touching both the upper and the left walls -> its x and y speed must be set positive
        Point2D.Double pos = new Point2D.Double(-GameSettings.EPS, -GameSettings.EPS);
        Point2D.Double vel = new Point2D.Double(-1, -1);
        Ball ball = new Ball(pos, vel, 10, BallState.IN_PLAY, null);
        ball.bounceOffWalls();
        assertEquals(ball.getVelocity().y, 1, 0.0001);
        assertEquals(ball.getVelocity().x, 1, 0.0001);
    }
}
