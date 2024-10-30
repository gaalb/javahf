package model;

import engine.GameEngine;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class CollideableObject {
    protected ObjectSpot spot;
    private GameEngine gameEngine;

    public CollideableObject(ObjectSpot spot, GameEngine gameEngine) {
        this.spot = spot;
        this.gameEngine = gameEngine;
    }

    public ObjectSpot getSpot() {
        return spot;
    }

    public void setSpot(ObjectSpot spot) {
        this.spot = spot;
    }

    public abstract Point2D.Double getCollisionPoint(Ball ball);

    public void destroy() {
        this.spot.clearObject();
        this.spot = null;
        List<CollideableObject> objects = gameEngine.getGameData().getObjects();
        objects.remove(this);
    }
}
