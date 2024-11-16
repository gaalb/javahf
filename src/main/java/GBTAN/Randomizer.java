package GBTAN;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represents a boon that randomizes the trajectory of balls that collide with it.
 * Each ball is affected only once, and its velocity is adjusted to a new random angle.
 */
public class Randomizer extends Boon {
    /**
     * The random number generator used to determine the new direction of the ball.
     */
    private final Random random;

    /**
     * A list of balls that have already been affected by this randomizer.
     * Ensures that each ball is affected only once.
     */
    private final List<Ball> affectedBalls;

    /**
     * Constructs a new Randomizer boon with a specified radius and game instance.
     *
     * @param radius The radius of the Randomizer.
     * @param game   The game instance this Randomizer is associated with.
     */
    public Randomizer(double radius, Game game) {
        super(radius, game);
        type = ObjectType.RANDOMIZER;
        random = new Random();
        affectedBalls = new LinkedList<>();
    }

    /**
     * Randomizes the trajectory of the specified ball. If the ball has already
     * been affected by this randomizer, it is ignored. Otherwise, its velocity
     * is adjusted to a random angle.
     *
     * @param ball The ball to be affected by this randomizer.
     */
    @Override
    public void affect(Ball ball) {
        if (!spent) spent = true;
        if (!affectedBalls.contains(ball)) {
            double angle = random.nextDouble()*Math.PI;
            double vel = ball.getAbsVelocity();
            ball.setVelocity(new Point2D.Double(vel*Math.cos(angle), -vel*Math.sin(angle)));
            affectedBalls.add(ball);
        }
    }
}
