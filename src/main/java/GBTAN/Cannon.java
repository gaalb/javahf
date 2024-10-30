package GBTAN;

import GBTAN.Ball.BallState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class Cannon {
    private double aimAngle;
    private Point2D.Double position;
    private Game game;
    private List<Ball> storedBalls;

    public Cannon(int xPosition, double aimAngle, Game game) {
        this.game = game;
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
        private Timer timer;
        public FireHandler(int framesPerShot, Timer timer) {
            frameCounter = framesPerShot;
            this.framesPerShot = framesPerShot;
            this.timer = timer;
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
                timer.removeActionListener(this);
            }
        }
    }

    public void fireAll() {
        Timer timer = game.getPhysicsEngine().getPhysicsTimer();
        FireHandler fireHandler = new FireHandler(GameSettings.FRAMES_BETWEEN_BALLS, timer);
        timer.addActionListener(fireHandler);
    }

    public void storeBall(Ball b) {
        storedBalls.add(b);
        b.setState(BallState.IN_STORE);
        b.setPosition(position);
    }

    public Point2D.Double project() {
        Cannon cannon = game.getGameData().getCannon();
        double aimAngle = cannon.getAimAngle();
        double vx = GameSettings.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
        double vy = -GameSettings.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
        Ball ball = new Ball(cannon.getPosition(), new Point2D.Double(vx, vy), GameSettings.BALL_RADIUS, BallState.IN_PLAY, game);
        while (true) {
            ball.move();
            if (ball.getPosition().x < ball.getRadius()) break;
            if (ball.getPosition().x > GameSettings.GAME_WIDTH - ball.getRadius()) break;
            if (ball.getPosition().y < ball.getRadius()) break;
            if (game.getPhysicsEngine().firstCollisionPoint(ball) != null) break;
        }
        return ball.getPosition();
    }

    public static class CannonConfig {
        public int x;
        public double angle;
        public int ballNum;
        public CannonConfig(int x, double angle, int ballNum) {
            this.x = x;
            this.angle = angle;
            this.ballNum = ballNum;
        }
    }
}
