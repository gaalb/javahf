package v2.Model;

import v2.Controller.*;
import v2.View.*;

import java.awt.geom.Point2D;

public abstract class CollideableObject {
    protected ObjectSpot spot;

    public CollideableObject(ObjectSpot spot) {
        this.spot = spot;
    }

    public ObjectSpot getSpot() {
        return spot;
    }

    public void setSpot(ObjectSpot spot) {
        this.spot = spot;
    }

    public abstract Point2D.Double getCollisionPoint(Ball ball);
}
