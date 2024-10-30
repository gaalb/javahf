package GBTAN;

import java.awt.geom.Point2D;

public class ObjectSpot {
    private final Point2D.Double center;
    private final Point2D.Double[] corners;
    private final double sideLength;
    private CollideableObject object;

    public ObjectSpot(Point2D.Double center, double sideLength) {
        this.center = center;
        this.sideLength = sideLength;
        this.corners = calculateCorners(center, sideLength);
    }

    /*
    * Order of corners:
    * 3---2
    * |   |
    * 0---1
    * */
    private static Point2D.Double[] calculateCorners(Point2D.Double center, double sideLength) {
        double halfSide = sideLength / 2.0;
        return new Point2D.Double[]{
                new Point2D.Double(center.x - halfSide, center.y + halfSide),  // Bottom-left
                new Point2D.Double(center.x + halfSide, center.y + halfSide), // Bottom-right
                new Point2D.Double(center.x + halfSide, center.y - halfSide), // Top-right
                new Point2D.Double(center.x - halfSide, center.y - halfSide) // Top-left
        };
    }

    public Point2D.Double getCenter() {
        return new Point2D.Double(center.x, center.y);
    }

    public double getSideLength() {
        return sideLength;
    }

    public Point2D.Double[] getCorners() {
        return corners.clone();  // Return a clone to prevent external modification
    }

    public boolean isEmpty() {
        return object == null;
    }

    public CollideableObject getObject() {
        return object;
    }

    public void setObject(CollideableObject object) {
        this.object = object;
        object.setSpot(this);
    }

    public void clearObject() {
        this.object = null;
    }

}
