package model;

import engine.GameEngine;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

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
    private GameEngine gameEngine;
    private int health;

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

    public Block(BlockType type, ObjectSpot spot, int health, GameEngine gameEngine) {
        super(spot);
        this.type = type;
        this.health = health;
        this.gameEngine = gameEngine;
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

    public Point2D.Double getPosition() {
        return spot.getCenter();
    }

    public Line2D.Double[] getSides() {
        int numCorners = corners.length;
        Line2D.Double[] sides = new Line2D.Double[numCorners];
        for (int i = 0; i < numCorners; i++) {
            sides[i] = new Line2D.Double(corners[i], corners[(i + 1) % numCorners]);
        }
        return sides;
    }

    public Polygon getPolygon() {
        Point2D.Double[] points = getCorners();
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i=0; i<points.length; i++) {
            xPoints[i] = (int)Math.round(points[i].x);
            yPoints[i] = (int)Math.round(points[i].y);
        }
        return new Polygon(xPoints, yPoints, points.length);
    }

    public static class BlockConfig {
        BlockType type;
        int x;
        int y;
        public BlockConfig(int x, int y, BlockType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    public void decrementHealth() {
        this.health -= 1;
        if (this.health == 0) {
            List<Block> blocks = gameEngine.getGameData().getBlocks();
            blocks.remove(this);
        }
    }

    public int getHealth() {
        return health;
    }
}
