package GBTAN;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class CollideableObject {
    protected ObjectSpot spot;
    protected Game game;

    public CollideableObject(ObjectSpot spot, Game game) {
        this.spot = spot;
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
