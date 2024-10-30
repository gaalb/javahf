package model;

import engine.CollisionDetection;
import engine.GameEngine;

import java.awt.geom.Point2D;

public class Boon extends CollideableObject {

    @Override
    public Point2D.Double getCollisionPoint(Ball ball) {
        return new Point2D.Double(0, 0);
    }

    public Boon(ObjectSpot spot, GameEngine gameEngine) {
        super(spot, gameEngine);
    }


}
