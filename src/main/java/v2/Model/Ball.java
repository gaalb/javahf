package v2.Model;

import v2.View.*;
import v2.Controller.*;

import java.awt.geom.Point2D;

public class Ball {
    public enum BallState {
        IN_STORE, IN_PLAY, RETURNED;
    }

    private Point2D.Double position;
    private Point2D.Double velocity;
    private double radius;
    private BallState state;

    public Ball(Point2D.Double position, Point2D.Double velocity, double radius, BallState state) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.state = state;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public Point2D.Double getVelocity() {
        return velocity;
    }

    public double getRadius() {
        return radius;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    public void setVelocity(Point2D.Double velocity) {
        this.velocity = velocity;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setState(BallState state) {
        this.state = state;
    }

    public BallState getState() {
        return state;
    }

    public void handleWallCollision(GameData gameData) {
        if (position.x < radius) velocity.x *= -1; // left wall
        if (position.x > GameData.GAME_WIDTH - radius) velocity.x *= -1;  // right wall
        if (position.y < radius) velocity.y *= -1;  // top wall
        if (position.y > GameData.GAME_HEIGHT + radius) {
            Cannon cannon = gameData.getPlayer().getCannon();
            cannon.getBallsReturned().add(this);
        }
    }

    public void update(GameData gameData) {
        double timestep = 1.0/GameData.FPS;
        position.x += velocity.x * timestep;
        position.y += velocity.y * timestep;
        handleWallCollision(gameData);
    }

}
