package GBTAN;

import java.awt.geom.Point2D;

public abstract class CollideableObject {
    public enum ObjectType {
        SQUARE,
        TRIANGLE_LOWER_LEFT,
        TRIANGLE_LOWER_RIGHT,
        TRIANGLE_UPPER_LEFT,
        TRIANGLE_UPPER_RIGHT,
        RANDOMIZER,
        PLUS_ONE,
        NULL
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
