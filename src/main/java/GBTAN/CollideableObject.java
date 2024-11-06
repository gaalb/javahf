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

    public CollideableObject(Game game) {
        this.spot = null;
        this.game = game;
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
