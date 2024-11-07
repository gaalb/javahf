package GBTAN;

import GBTAN.CollideableObject.ObjectType;

public class PlusOne extends Boon {
    public PlusOne(double radius, Game game) {
        super(radius, game);
        type = ObjectType.PLUS_ONE;
    }

    @Override
    public void affect(Ball ball) {
        if (!spent) spent = true;
    }
}
