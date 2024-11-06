package GBTAN;

import java.awt.geom.Point2D;
import GBTAN.Ball.BallState;

public class PlusOne extends Boon {
    public PlusOne(double radius, Game game) {
        super(radius, game);
    }

    @Override
    public void bless(Ball ball) {
        if (!spent) spent = true;
    }
}
