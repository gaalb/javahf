package GBTAN;

import java.awt.geom.Point2D;

public abstract class CollideableObject {
    public enum ObjectType {
        SQUARE,  // Block
        TRIANGLE_LOWER_LEFT,  // Block
        TRIANGLE_LOWER_RIGHT,  // Block
        TRIANGLE_UPPER_LEFT,  // Block
        TRIANGLE_UPPER_RIGHT,  // Block
        RANDOMIZER,  // Boon
        PLUS_ONE,  // Boon
        NULL  // Empty spot
    }

    protected ObjectSpot spot;
    protected Game game;
    protected ObjectType type;

    public CollideableObject(Game game) {
        this.type = ObjectType.NULL;
        this.spot = null;
        this.game = game;
    }

    public ObjectType getType() {
        return type;
    }

    public ObjectSpot getSpot() {
        return spot;
    }

    public void setSpot(ObjectSpot spot) {
        this.spot = spot;
    }

    public Point2D.Double getPosition() {
        return spot.getCenter();
    }
}
