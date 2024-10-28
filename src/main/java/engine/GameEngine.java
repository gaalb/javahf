package engine;

import model.*;
import model.GameData.GameState;
import display.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class GameEngine {
    private GameData gameData;
    private GameFrame gameFrame;
    private javax.swing.Timer physicsTimer;

    private AimHandler aimHandler;

    public GameEngine() {
        gameData = new GameData(this);
        gameFrame = new GameFrame(this);
        aimHandler = new AimHandler(this);
        physicsTimer = new Timer(GameSettings.PHYSICS_STEP_MS, null);
        physicsTimer.start();
        physicsTimer.addActionListener(this::updateGameState);
    }

    private void handleWallCollision(Ball ball) {
        Point2D.Double v = ball.getVelocity();
        if (ball.getPosition().x < ball.getRadius()) ball.setVelocity(Math.abs(v.x), v.y); // left wall
        if (ball.getPosition().x > GameSettings.GAME_WIDTH - ball.getRadius()) ball.setVelocity(-Math.abs(v.x), v.y);  // right wall
        if (ball.getPosition().y < ball.getRadius()) ball.setVelocity(v.x, Math.abs(v.y));  // top wall
        if (ball.getPosition().y > GameSettings.GAME_HEIGHT + ball.getRadius()) {  // bottom wall
            ball.setState(Ball.BallState.RETURNED);
            List<Ball> ballsInPlay = gameData.getBallsInPlay();
            if (ballsInPlay.isEmpty()) {
                changeGameState(GameState.AIMING);
            }
        }
    }

    private void bounceBallOffPoint(Ball ball, Point2D.Double point) {
        Point2D.Double ballCenter = ball.getPosition();
        Point2D.Double direction = new Point2D.Double(point.x-ballCenter.x, point.y-ballCenter.y);
        double magnitudeDirection = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        Point2D.Double unitDirection = new Point2D.Double(direction.x / magnitudeDirection, direction.y / magnitudeDirection);
        Point2D.Double v = ball.getVelocity();
        double dot = v.x * unitDirection.x + v.y * unitDirection.y;
        Point2D.Double projection = new Point2D.Double(dot * unitDirection.x, dot*unitDirection.y);
        Point2D.Double perpendicular = new Point2D.Double(v.x - projection.x, v.y-projection.y);
        Point2D.Double vNew = new Point2D.Double(perpendicular.x - projection.x, perpendicular.y - projection.y);
        ball.setVelocity(vNew);
    }

    private SimpleEntry<Block, Point2D.Double> firstCollisionPoint(Ball ball) {
        double minCollisionDistance = ball.getRadius() + GameSettings.BLOCK_WIDTH/Math.sqrt(2) + CollisionDetection.eps;
        for (Block block: gameData.getBlocks()) {
            if (ball.getPosition().distance(block.getPosition()) <= minCollisionDistance) {
                Point2D.Double collisionPoint = CollisionDetection.ballBlockCollisionPoint(ball, block);
                if (collisionPoint != null) {
                    return new SimpleEntry<>(block, collisionPoint);
                }
            }
        }
        return null;
    }

    private void updateBall(Ball ball) {
        ball.move();
        handleWallCollision(ball);  // change direction from walls
        SimpleEntry<Block, Point2D.Double> blockAndPoint = firstCollisionPoint(ball);
        while (blockAndPoint !=  null) {
            Block block = blockAndPoint.getKey();
            Point2D.Double collisionPoint = blockAndPoint.getValue();
            bounceBallOffPoint(ball, collisionPoint);
            while (CollisionDetection.ballBlockCollisionPoint(ball, block) != null) {
                ball.move();
            }
            blockAndPoint = firstCollisionPoint(ball);
        }
    }

    public void updateGameState(ActionEvent event) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (Ball ball: ballsInPlay) {
            updateBall(ball);
        }
    }

    public Timer getTimer() {
        return physicsTimer;
    }

    public GameData getGameData() {
        return gameData;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void changeGameState(GameState newState) {
        gameData.setGameState(newState);
        if (newState == GameState.AIMING) {
            gameFrame.getGamePanel().addMouseListener(aimHandler);
            gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
            Cannon cannon = gameData.getCannon();
            List<Ball> balls = gameData.getBalls();
            for (Ball b: balls) {
                if (b.getState() == Ball.BallState.RETURNED) {
                    cannon.storeBall(b);
                }
            }
        }
        if (newState == GameState.PLAYING) {
            gameFrame.getGamePanel().removeMouseListener(aimHandler);
            gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
        }
    }
}
