package GBTAN;

import java.awt.geom.Point2D;

/**
 * Represents a spot in the game grid where objects (such as blocks or boons) can be placed.
 * Each spot is defined by its center position, side length, and the object it contains.
 */
public class ObjectSpot {
    /**
     * The center point of this spot.
     */
    private final Point2D.Double center;

    /**
     * The four corners of this spot, calculated based on its center and side length.
     * Order of corners:<br>
     * 3-2<br>
     * 0-1
     */
    private final Point2D.Double[] corners;

    /**
     * The side length of this spot.
     */
    private final double sideLength;

    /**
     * The object currently occupying this spot, or {@code null} if the spot is empty.
     */
    private CollideableObject object;

    /**
     * Constructs an ObjectSpot with the specified center and side length.
     *
     * @param center     The center point of the spot.
     * @param sideLength The side length of the spot.
     */
    public ObjectSpot(Point2D.Double center, double sideLength) {
        this.center = center;
        this.sideLength = sideLength;
        this.corners = calculateCorners(center, sideLength);
    }

    /**
     * Calculates the corners of the spot based on its center and side length.
     *
     * @param center     The center point of the spot.
     * @param sideLength The side length of the spot.
     * @return An array of four points representing the corners.
     */
    private static Point2D.Double[] calculateCorners(Point2D.Double center, double sideLength) {
        double halfSide = sideLength / 2.0;
        return new Point2D.Double[]{
                new Point2D.Double(center.x - halfSide, center.y + halfSide),  // Bottom-left
                new Point2D.Double(center.x + halfSide, center.y + halfSide), // Bottom-right
                new Point2D.Double(center.x + halfSide, center.y - halfSide), // Top-right
                new Point2D.Double(center.x - halfSide, center.y - halfSide)  // Top-left
        };
    }

    /**
     * Returns the center point of the spot.
     *
     * @return A new {@code Point2D.Double} representing the center.
     */
    public Point2D.Double getCenter() {
        return new Point2D.Double(center.x, center.y);
    }

    /**
     * Returns the side length of the spot.
     *
     * @return The side length.
     */
    public double getSideLength() {
        return sideLength;
    }

    /**
     * Returns the corners of the spot.
     *
     * @return A clone of the array containing the corners.
     */
    public Point2D.Double[] getCorners() {
        return corners.clone();  // Return a clone to prevent external modification
    }

    /**
     * Checks if the spot is empty.
     *
     * @return {@code true} if the spot is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return object == null;
    }

    /**
     * Returns the object currently occupying the spot.
     *
     * @return The {@code CollideableObject} in the spot, or {@code null} if the spot is empty.
     */
    public CollideableObject getObject() {
        return object;
    }

    /**
     * Sets the object occupying the spot.
     *
     * @param object The {@code CollideableObject} to set, or {@code null} to clear the spot.
     */
    public void setObject(CollideableObject object) {
        this.object = object;
        if (object != null) object.setSpot(this);
    }

    /**
     * Clears the object from the spot, making it empty.
     */
    public void clearObject() {
        this.object = null;
    }
}
