package GBTAN;
import GBTAN.Ball.BallState;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;

public class CannonTest {

    /**
     * Test if a cannon can correctly store and fire balls.
     */
    @Test
    public void testFire() {
        Cannon cannon = new Cannon(210, 90, null);
        Ball b = new Ball(new Point2D.Double(0, 0), new Point2D.Double(0, 0), GameSettings.BALL_RADIUS,
                BallState.RETURNED, null);
        // Initially empty
        assertEquals(cannon.storedNumber(), 0);
        cannon.storeBall(b);
        // One ball stored
        assertEquals(cannon.storedNumber(), 1);
        cannon.fireSingleBall();
        // Empty again after firing
        assertEquals(cannon.storedNumber(), 0);
        // Since the cannon pointed upwards, ball should have an upwards velocity now
        assertEquals(b.getVelocity().y, -GameSettings.BALL_SPEED, 0.0001);
        assertEquals(b.getVelocity().x, 0, 0.0001);
    }
}
