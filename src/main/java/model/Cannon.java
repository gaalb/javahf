package model;

import engine.GameEngine;
import model.Ball.BallState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class Cannon {
    private double aimAngle;
    private Point2D.Double position;
    private GameEngine gameEngine;
    private List<Ball> storedBalls;

    public Cannon(int xPosition, double aimAngle, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        position = new Point2D.Double(xPosition, GameSettings.GAME_HEIGHT);
        this.aimAngle = aimAngle;
        this.storedBalls = new LinkedList<>();
    }

    public double getAimAngle() {
        return aimAngle;
    }

    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
    }

    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y);
    }

    public void setPosition(double xPosition) {
        this.position = new Point2D.Double(xPosition, position.y);
    }

    public void setPosition(double x, double y) {
        this.position = new Point2D.Double(x, y);
    }

    public int storedNumber() {
        return storedBalls.size();
    }

    private void fireSingleBall() {
        Ball b = storedBalls.removeFirst();
        b.setState(Ball.BallState.IN_PLAY);
        double vx = GameSettings.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
        double vy = -GameSettings.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
        b.setVelocity(new Point2D.Double(vx, vy));
    }

    private class FireHandler implements ActionListener {
        private int frameCounter;
        private int framesPerShot;
        public FireHandler(int framesPerShot) {
            frameCounter = framesPerShot;
            this.framesPerShot = framesPerShot;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!storedBalls.isEmpty()) {
                if (frameCounter >= framesPerShot) {
                    fireSingleBall();
                    frameCounter = 0;
                }
                frameCounter++;
            } else {
                gameEngine.getTimer().removeActionListener(this);
            }
        }
    }

    public void fireAll() {
        FireHandler fireHandler = new FireHandler(GameSettings.FRAMES_BETWEEN_BALLS);
        gameEngine.getTimer().addActionListener(fireHandler);
    }

    public void storeBall(Ball b) {
        storedBalls.add(b);
        b.setState(BallState.IN_STORE);
        b.setPosition(position);
    }

    public Point2D.Double project() {
        Cannon cannon = gameEngine.getGameData().getCannon();
        double aimAngle = cannon.getAimAngle();
        double vx = GameSettings.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
        double vy = -GameSettings.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
        Ball ball = new Ball(cannon.getPosition(), new Point2D.Double(vx, vy), GameSettings.BALL_RADIUS, BallState.IN_PLAY, gameEngine);
        while (true) {
            ball.move();
            if (ball.getPosition().x < ball.getRadius()) break;
            if (ball.getPosition().x > GameSettings.GAME_WIDTH - ball.getRadius()) break;
            if (ball.getPosition().y < ball.getRadius()) break;
            if (gameEngine.firstCollisionPoint(ball) != null) break;
        }
        return ball.getPosition();
    }
}
