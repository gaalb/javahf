package GBTAN;

/**
 * Represents a boon in the game, which provides special effects when interacted with.
 * Boons are a type of {@link CollideableObject} and have a defined radius and state.
 */
public abstract class Boon extends CollideableObject {

    /**
     * The radius of the boon, defining its size.
     */
    private final double radius;

    /**
     * Indicates whether the boon has already been activated (spent).
     */
    protected boolean spent;

    /**
     * Creates a new boon with the specified radius and game reference.
     *
     * @param radius the radius of the boon.
     * @param game   the game instance this boon belongs to.
     */
    public Boon(double radius, Game game) {
        super(game);
        this.radius = radius;
        this.spent = false;
    }

    /**
     * Gets the radius of the boon.
     *
     * @return the radius of the boon.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Applies the effect of the boon to the specified ball.
     * The exact behavior is determined by the specific type of boon.
     *
     * @param ball the ball interacting with the boon.
     * @see Ball
     */
    abstract public void affect(Ball ball);

    /**
     * Checks whether the boon has already been activated.
     *
     * @return {@code true} if the boon has been activated, otherwise {@code false}.
     */
    public boolean isSpent() {
        return spent;
    }
}
