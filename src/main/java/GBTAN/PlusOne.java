package GBTAN;

import java.awt.geom.Point2D;
import GBTAN.Ball.BallState;

public class PlusOne extends Boon{
    public PlusOne(double radius, ObjectSpot spot, Game game) {
        super(radius, spot, game);
    }

    @Override
    public void bless(Ball ball) {
        if (!spent) {
            Ball newBall = new Ball(new Point2D.Double(0,0), new Point2D.Double(0,0),
                                    GameSettings.BALL_RADIUS, BallState.RETURNED, game);
            game.getGameData().getBalls().add(newBall);
            spent = true;
        }
    }
}
