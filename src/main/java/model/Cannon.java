package model;

import engine.GameEngine;
import model.GameData.GameState;
import model.Ball.BallState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class Cannon {
    private double aimAngle;
    private Point position;
    private GameEngine gameEngine;

    private List<Ball> storedBalls;

    public Cannon(int xPosition, double aimAngle, List<Ball> storedBalls, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        position = new Point(xPosition, GameSettings.GAME_HEIGHT);
        this.aimAngle = aimAngle;
        this.storedBalls = storedBalls;
    }

    public double getAimAngle() {
        return aimAngle;
    }

    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public List<Ball> getStoredBalls() {
        return storedBalls;
    }

    private class FireHandler implements ActionListener {
        private int frameCounter;
        private int framesPerShot;
        List<Ball> ballsInPlay;
        Timer timer;
        public FireHandler(List<Ball> ballsInPlay, int framesPerShot, Timer timer) {
            frameCounter = framesPerShot;
            this.framesPerShot = framesPerShot;
            this.ballsInPlay = ballsInPlay;
            this.timer = timer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!storedBalls.isEmpty()) {
                if (frameCounter >= framesPerShot) {
                    Ball b = storedBalls.removeFirst();
                    b.setState(Ball.BallState.IN_PLAY);
                    double vx = GameSettings.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
                    double vy = -GameSettings.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
                    b.setVelocity(new Point2D.Double(vx, vy));
                    ballsInPlay.add(b);
                    frameCounter = 0;
                }
                frameCounter++;
            } else {
                timer.removeActionListener(this);
            }
        }
    }

    public void fireAll() {
        List<Ball> ballsInPlay = gameEngine.getGameData().getBallsInPlay();
        Timer timer = gameEngine.getTimer();
        FireHandler fireHandler = new FireHandler(ballsInPlay, 50, timer);
        timer.addActionListener(fireHandler);
        System.out.println("FIRED CANNON");
    }
}
