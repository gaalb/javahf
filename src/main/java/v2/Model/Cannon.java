package v2.Model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class Cannon {
    private double aimAngle;
    private Point position;
    private int circleRadius;

    private List<Ball> ballsInStore;
    private List<Ball> ballsReturned;

    public Cannon(int xPosition, double aimAngle, int ballsInStore) {
        this.position = new Point(xPosition, GameData.GAME_HEIGHT);
        this.aimAngle = aimAngle;
        this.ballsInStore = new LinkedList<>();
        this.ballsReturned = new LinkedList<>();
        for (int i=0; i<ballsInStore; i++) {
            Point2D.Double p = new Point2D.Double(xPosition, position.getY());
            Point2D.Double v = new Point2D.Double(0, 0);
            this.ballsInStore.add(new Ball(p, v, GameData.BALL_RADIUS, Ball.BallState.IN_STORE));
        }
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

    public List<Ball> getBallsInStore() {
        return ballsInStore;
    }

    public List<Ball> getBallsReturned() {
        return ballsReturned;
    }

    private class FireHandler implements ActionListener {
        private GameData gameData;
        private int frameCounter;
        private int framesPerShot;
        private Timer timer;
        public FireHandler(GameData gameData, int framesPerShot, Timer timer) {
            frameCounter = framesPerShot;
            this.framesPerShot = framesPerShot;
            this.gameData = gameData;
            this.timer = timer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!ballsInStore.isEmpty()) {
                if (frameCounter >= framesPerShot) {
                    Ball b = ballsInStore.removeFirst();
                    b.setState(Ball.BallState.IN_PLAY);
                    double vx = GameData.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
                    double vy = -GameData.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
                    b.setVelocity(new Point2D.Double(vx, vy));
                    gameData.getBallsInPlay().add(b);
                    frameCounter = 0;
                    System.out.println("Fired ball. Balls in store: " + ballsInStore.size() + ", balls in play: " +
                            gameData.getBallsInPlay().size());
                }
                frameCounter++;
            } else {
                System.out.println("REMOVING FIRE LISTENER");
                timer.removeActionListener(this);
            }
        }
    }

    public void fireAll(GameData gameData, Timer timer) {
        FireHandler fireHandler = new FireHandler(gameData, 100, timer);
        timer.addActionListener(fireHandler);
        System.out.println("FIRING CANNON REEEEEEEEEEEEE");
    }
}
