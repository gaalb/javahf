package GBTAN;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Block extends CollideableObject {
    private Point2D.Double[] corners;
    private int health;

    private void setCornersToSpot() {
        // The corners of a Block are determined by its type (shape) and the
        // corners of the containing spot. If we wanted to make the block
        // smaller than the spot it's in, this is the method to modify.
        Point2D.Double[] spotCorners = spot.getCorners();
        switch (type) {
            case TRIANGLE_LOWER_LEFT:
                corners = new Point2D.Double[] {
                    spotCorners[0], spotCorners[1], spotCorners[3]
                };
                return;
            case TRIANGLE_LOWER_RIGHT:
                corners = new Point2D.Double[] {
                        spotCorners[0], spotCorners[1], spotCorners[2]
                };
                return;
            case TRIANGLE_UPPER_RIGHT:
                corners = new Point2D.Double[] {
                        spotCorners[1], spotCorners[2], spotCorners[3]
                };
                return;
            case TRIANGLE_UPPER_LEFT:
                corners = new Point2D.Double[] {
                        spotCorners[0], spotCorners[2], spotCorners[3]
                };
                return;
            default:
                corners = spotCorners;
                return;
        }
    }

    public Block(ObjectType type, int health, Game game) {
        super(game);
        this.type = type;
        this.health = health;
    }

    public void setType(ObjectType type) {
        this.type = type;
        setCornersToSpot();
    }

    @Override
    public void setSpot(ObjectSpot spot) {
        super.setSpot(spot);
        if (spot != null) {
            setCornersToSpot();
        }
    }

    public Point2D.Double[] getCorners() {
        return corners.clone();
    }

    public Line2D.Double[] getSides() {
        int numCorners = corners.length;
        Line2D.Double[] sides = new Line2D.Double[numCorners];
        for (int i = 0; i < numCorners; i++) {
            sides[i] = new Line2D.Double(corners[i], corners[(i + 1) % numCorners]);
        }
        return sides;
    }

    public Polygon getPolygon() { // required for displaying
        Point2D.Double[] points = getCorners();
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i=0; i<points.length; i++) {
            xPoints[i] = (int)Math.round(points[i].x);
            yPoints[i] = (int)Math.round(points[i].y);
        }
        return new Polygon(xPoints, yPoints, points.length);
    }

    public void decrementHealth() {
        this.health -= 1;
        if (this.health <= 0) { // dies at 0 hp
            game.getGameData().destroyObject(this);
        }
    }

    public int getHealth() {
        return health;
    }
}
