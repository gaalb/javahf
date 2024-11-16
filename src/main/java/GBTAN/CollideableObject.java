package GBTAN;

import java.awt.geom.Point2D;

/**
 * Represents an abstract object in the game that the ball may collide with.
 * This class serves as a base for blocks and boons, defining common properties
 * such as position, type, and associated spot.
 */
public abstract class CollideableObject {

    /**
     * Defines the possible types of collideable objects in the game.
     */
    public enum ObjectType {
        /**
         * A square block.
         */
        SQUARE,

        /**
         * A block shaped as a triangle with the right angle in the lower-left corner.
         */
        TRIANGLE_LOWER_LEFT,

        /**
         * A block shaped as a triangle with the right angle in the lower-right corner.
         */
        TRIANGLE_LOWER_RIGHT,

        /**
         * A block shaped as a triangle with the right angle in the upper-left corner.
         */
        TRIANGLE_UPPER_LEFT,

        /**
         * A block shaped as a triangle with the right angle in the upper-right corner.
         */
        TRIANGLE_UPPER_RIGHT,

        /**
         * A boon that randomizes ball velocities.
         */
        RANDOMIZER,

        /**
         * A boon that increases the player's ball count.
         */
        PLUS_ONE,

        /**
         * Represents an empty spot.
         */
        NULL
    }

    /**
     * The spot on the game grid where this object is located.
     */
    protected ObjectSpot spot;

    /**
     * The game instance this object belongs to.
     */
    protected Game game;

    /**
     * The type of the object (e.g., block, boon, or empty spot).
     */
    protected ObjectType type;

    /**
     * Constructs a new collideable object with the specified game reference.
     * By default, the object type is set to {@link ObjectType#NULL}, and no spot is assigned.
     *
     * @param game the game instance this object belongs to.
     */
    public CollideableObject(Game game) {
        this.type = ObjectType.NULL;
        this.spot = null;
        this.game = game;
    }

    /**
     * Gets the type of the object.
     *
     * @return the object's type.
     * @see ObjectType
     */
    public ObjectType getType() {
        return type;
    }

    /**
     * Gets the spot on the game grid where this object is located.
     *
     * @return the object's spot, or {@code null} if no spot is assigned.
     * @see ObjectSpot
     */
    public ObjectSpot getSpot() {
        return spot;
    }

    /**
     * Sets the spot on the game grid for this object.
     *
     * @param spot the new spot for this object.
     * @see ObjectSpot
     */
    public void setSpot(ObjectSpot spot) {
        this.spot = spot;
    }

    /**
     * Gets the position of the object, determined by the center of its assigned spot.
     *
     * @return the position of the object as a {@link Point2D.Double}.
     * @see ObjectSpot#getCenter()
     */
    public Point2D.Double getPosition() {
        return spot.getCenter();
    }
}
