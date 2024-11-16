package GBTAN;

/**
 * Represents a boon that increments the number of balls the player controls.
 * A PlusOne boon is consumed upon collision with a ball.
 */
public class PlusOne extends Boon {

    /**
     * Constructs a PlusOne boon with the specified radius and game context.
     *
     * @param radius The radius of the PlusOne boon.
     * @param game   The game instance this PlusOne belongs to.
     */
    public PlusOne(double radius, Game game) {
        super(radius, game);
        type = ObjectType.PLUS_ONE;
    }

    /**
     * Defines the behavior when a ball interacts with this boon.
     * This implementation marks the boon as spent, meaning it cannot be reused.
     *
     * @param ball The ball that interacts with the PlusOne boon.
     */
    @Override
    public void affect(Ball ball) {
        if (!spent) spent = true;
    }
}
