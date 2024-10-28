package model;

import engine.GameEngine;
import model.GameData.GameState;

import java.awt.geom.Point2D;
import java.util.*;

public class Ball {
    public enum BallState {
        IN_STORE, IN_PLAY, RETURNED;
    }

    private Point2D.Double position;
    private Point2D.Double velocity;
    private double radius;
    private BallState state;
    private GameEngine gameEngine;

    public Ball(Point2D.Double position, Point2D.Double velocity, double radius, BallState state, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.state = state;
    }

    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y); // clone to avoid accidents
    }

    public Point2D.Double getVelocity() {
        return new Point2D.Double(velocity.x, velocity.y);
    }

    public double getRadius() {
        return radius;
    }

    public void setPosition(Point2D.Double position) {
        this.position = new Point2D.Double(position.x, position.y);
    }

    public void setPosition(double x, double y) {
        this.position = new Point2D.Double(x, y);
    }

    public void setVelocity(Point2D.Double velocity) {
        this.velocity = new Point2D.Double(velocity.x, velocity.y);
    }

    public void setVelocity(double x, double y) {
        this.velocity = new Point2D.Double(x, y);
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
}
