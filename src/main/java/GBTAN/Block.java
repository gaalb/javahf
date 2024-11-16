package GBTAN;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Represents a block in the game, which is a type of {@link CollideableObject}.
 * Blocks have specific shapes, health points, and are associated with a spot on the game grid.
 */
public class Block extends CollideableObject {

    /**
     * The corners of the block, defining its shape.
     */
    private Point2D.Double[] corners;

    /**
     * The current health of the block. When it reaches 0, the block is destroyed.
     */
    private int health;

    /**
     * Initializes a new block with the specified type, health, and game reference.
     *
     * @param type   the type of the block (e.g., square or triangle variants).
     * @param health the initial health of the block.
     * @param game   the game instance the block belongs to.
     */
    public Block(ObjectType type, int health, Game game) {
        super(game);
        this.type = type;
        this.health = health;
    }

    /**
     * Updates the block's type and recalculates its corners based on its spot.
     *
     * @param type the new type of the block.
     */
    public void setType(ObjectType type) {
        this.type = type;
        setCornersToSpot();
    }

    /**
     * Sets the block's spot on the game grid and recalculates its corners accordingly.
     *
     * @param spot the spot to assign to the block.
     * @see ObjectSpot#getCorners()
     */
    @Override
    public void setSpot(ObjectSpot spot) {
        super.setSpot(spot);
        if (spot != null) {
            setCornersToSpot();
        }
    }

    /**
     * Gets the corners of the block, which define its shape.
     *
     * @return an array of points representing the corners of the block.
     */
    public Point2D.Double[] getCorners() {
        return corners.clone();
    }

    /**
     * Gets the sides of the block as line segments connecting its corners.
     *
     * @return an array of {@link Line2D.Double} representing the sides of the block.
     */
    public Line2D.Double[] getSides() {
        int numCorners = corners.length;
        Line2D.Double[] sides = new Line2D.Double[numCorners];
        for (int i = 0; i < numCorners; i++) {
            sides[i] = new Line2D.Double(corners[i], corners[(i + 1) % numCorners]);
        }
        return sides;
    }

    /**
     * Converts the block's corners into a {@link Polygon} for rendering purposes.
     *
     * @return a polygon representation of the block's shape.
     */
    public Polygon getPolygon() {
        Point2D.Double[] points = getCorners();
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xPoints[i] = (int) Math.round(points[i].x);
            yPoints[i] = (int) Math.round(points[i].y);
        }
        return new Polygon(xPoints, yPoints, points.length);
    }

    /**
     * Reduces the block's health by 1. If the health reaches 0, the block is destroyed.
     */
    public void decrementHealth() {
        this.health -= 1;
        if (this.health <= 0) {
            game.getGameData().destroyObject(this);
        }
    }

    /**
     * Gets the current health of the block.
     *
     * @return the current health of the block.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Calculates and updates the block's corners based on its type and the corners of its assigned spot.
     */
    private void setCornersToSpot() {
        // The corners of a Block are determined by its type (shape) and the
        // corners of the containing spot. If we wanted to make the block
        // smaller than the spot it's in, this is the method to modify.
        Point2D.Double[] spotCorners = spot.getCorners();
        switch (type) {
            case TRIANGLE_LOWER_LEFT:
                corners = new Point2D.Double[]{
                        spotCorners[0], spotCorners[1], spotCorners[3]
                };
                return;
            case TRIANGLE_LOWER_RIGHT:
                corners = new Point2D.Double[]{
                        spotCorners[0], spotCorners[1], spotCorners[2]
                };
                return;
            case TRIANGLE_UPPER_RIGHT:
                corners = new Point2D.Double[]{
                        spotCorners[1], spotCorners[2], spotCorners[3]
                };
                return;
            case TRIANGLE_UPPER_LEFT:
                corners = new Point2D.Double[]{
                        spotCorners[0], spotCorners[2], spotCorners[3]
                };
                return;
            default:
                corners = spotCorners;
        }
    }
}
