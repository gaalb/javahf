package GBTAN;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Randomizer extends Boon{
    private Random random;
    private List<Ball> affectedBalls;

    public Randomizer(double radius, Game game) {
        super(radius, game);
        type = ObjectType.RANDOMIZER;
        random = new Random();
        affectedBalls = new LinkedList<>();
    }

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
