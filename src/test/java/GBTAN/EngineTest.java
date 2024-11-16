package GBTAN;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;
import java.util.AbstractMap;

public class EngineTest {
    Game game;
    PhysicsEngine engine;

    @BeforeEach
    public void setUp() {
        GameSave save = new GameSave(GameSettings.DEFAULT_SAVE_FILE);
        Player player = new Player(GameSettings.DEFAULT_PLAYER_FILE);
        game = new Game(player, save);
        engine = game.getPhysicsEngine();
    }

    /**
     * Test if the static method can determine if a point falls on a line.
     */
    @Test
    public void testPointOnLine() {
        Point2D.Double startPoint = new Point2D.Double(-1, -1);
        Point2D.Double endPoint = new Point2D.Double(1, 1);
        // The line is diagonal -> the origin is on it
        Point2D.Double onLine = new Point2D.Double(0, 0);
        // However, a point off center is not on it
        Point2D.Double notOnLine = new Point2D.Double(0, 1);
        assertTrue(PhysicsEngine.pointOnLine(onLine, startPoint, endPoint));
        assertFalse(PhysicsEngine.pointOnLine(notOnLine, startPoint, endPoint));
    }

    /**
     * Test if the static method can determine if a ball is colliding with a block.
     */
    @Test
    public void testBallBlockCollision() {
        Point2D.Double ballPos = new Point2D.Double(100, 100);
        Point2D.Double ballVel = new Point2D.Double(1, 1);
        Ball ball = new Ball(ballPos, ballVel, 10, Ball.BallState.IN_PLAY, game);
        // Spot1 is barely touching the ball on the right
        ObjectSpot spot1 = new ObjectSpot(new Point2D.Double(135-GameSettings.EPS, 100), 50);
        // Spot2 is further to the right, not touching the ball
        ObjectSpot spot2 = new ObjectSpot(new Point2D.Double(135+GameSettings.EPS, 100), 50);
        Block block = new Block(CollideableObject.ObjectType.TRIANGLE_LOWER_LEFT, 10, game);
        spot1.setObject(block);
        Point2D.Double collisionPoint = PhysicsEngine.ballBlockCollisionPoint(ball, block);
        assertNotNull(collisionPoint);
        assertEquals(collisionPoint.x, 110, GameSettings.EPS);
        assertEquals(collisionPoint.y, 100, GameSettings.EPS);
        spot2.setObject(block);
        assertNull(PhysicsEngine.ballBlockCollisionPoint(ball, block));
    }

    @Test
    public void testFirstCollision() {
        Block block1 = new Block(CollideableObject.ObjectType.SQUARE, 10, game);
        Block block2 = new Block(CollideableObject.ObjectType.SQUARE, 10, game);
        // The center spot is located at the middle of the first row
        ObjectSpot centerSpot = game.getGameData().getSpots()[0][3];
        // The right spot is next to it on the right
        ObjectSpot rightSpot = game.getGameData().getSpots()[0][4];
        centerSpot.setObject(block1);
        rightSpot.setObject(block2);
        double r = 10;
        // The ball is going to be heading right, colliding with the center spot.
        double ballX = centerSpot.getCenter().x-centerSpot.getSideLength()/2-r+GameSettings.EPS;
        Point2D.Double ballPos = new Point2D.Double(ballX, centerSpot.getSideLength()/2);
        Point2D.Double ballVel = new Point2D.Double(GameSettings.BALL_SPEED, 0);
        Ball ball = new Ball(ballPos, ballVel, r, Ball.BallState.IN_PLAY, game);
        AbstractMap.SimpleEntry<Block, Point2D.Double> firstCollision = engine.firstCollisionPoint(ball);
        assertNotNull(firstCollision);
        assertSame(firstCollision.getKey(), block1);
        assertEquals(firstCollision.getValue().x, ballPos.x+ball.getRadius(), GameSettings.EPS);
        assertEquals(firstCollision.getValue().y, centerSpot.getSideLength()/2, GameSettings.EPS);
    }
}
