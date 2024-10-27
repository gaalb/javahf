package v2.Model;

import v2.View.*;
import v2.Controller.*;

import java.awt.geom.Point2D;

public class Block extends CollideableObject {
    public enum BlockType {
        SQUARE,
        TRIANGLE_LOWER_LEFT,
        TRIANGLE_LOWER_RIGHT,
        TRIANGLE_UPPER_LEFT,
        TRIANGLE_UPPER_RIGHT
    }
    private Point2D.Double[] corners;
    private BlockType type;
    private ObjectSpot spot;

    private void setCornersToSpot() {
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

    public Block(BlockType type, ObjectSpot spot) {
        super(spot);
        this.type = type;
        setCornersToSpot();
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
        setCornersToSpot();
    }

    @Override
    public void setSpot(ObjectSpot spot) {
        super.setSpot(spot);
        setCornersToSpot();
    }

    public Point2D.Double[] getCorners() {
        return corners.clone();
    }

    @Override
    public Point2D.Double getCollisionPoint(Ball ball) {
        return new Point2D.Double(0, 0);  // TODO
    }

}
