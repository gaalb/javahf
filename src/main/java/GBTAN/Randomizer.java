package GBTAN;

import java.awt.geom.Point2D;
import java.util.Random;

public class Randomizer extends Boon{
    private Random random;
    public Randomizer(double radius, Game game) {
        super(radius, game);
        random = new Random();
    }

    @Override
    public void bless(Ball ball) {
        spent = true;
        double angle = random.nextDouble()*Math.PI;
        double vel = ball.getAbsVelocity();
        ball.setVelocity(new Point2D.Double(vel*Math.cos(angle), -vel*Math.sin(angle)));
    }
}
