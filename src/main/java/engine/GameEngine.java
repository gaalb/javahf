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
    private GameStateSupervisor gameStateSupervisor;
    private javax.swing.Timer physicsTimer;
    private int physicsStepsPerTick;

    public GameEngine() {
        physicsTimer = new Timer(5, null);
        physicsStepsPerTick = GameSettings.PHYSICS_FREQ * physicsTimer.getDelay()/1000;
        physicsTimer.start();
        physicsTimer.addActionListener(this::updateGameState);
        gameData = new GameData(this);
        gameFrame = new GameFrame(this);
        gameStateSupervisor = new GameStateSupervisor(this);
    }

    private void ballLeavesPanel(Ball ball) {
        if (gameData.getBallsReturned().isEmpty()) {
            gameData.getCannon().setPosition(ball.getPosition().x);
        }
        ball.setState(Ball.BallState.RETURNED);
    }

    private void handleWallCollision(Ball ball) {
        Point2D.Double v = ball.getVelocity();
        if (ball.getPosition().x < ball.getRadius()) ball.setVelocity(Math.abs(v.x), v.y); // left wall
        if (ball.getPosition().x > GameSettings.GAME_WIDTH - ball.getRadius()) ball.setVelocity(-Math.abs(v.x), v.y);  // right wall
        if (ball.getPosition().y < ball.getRadius()) ball.setVelocity(v.x, Math.abs(v.y));  // top wall
        if (ball.getPosition().y > GameSettings.GAME_HEIGHT + ball.getRadius()) ballLeavesPanel(ball); // bottom wall
    }

    private void bounceBallOffPoint(Ball ball, Point2D.Double point) {
        // Calculates the portion of the ball's velocity which is parallel
        // to the line drawn between the ball's center and the collision
        // point, and inverts that component.
        Point2D.Double ballCenter = ball.getPosition();
        Point2D.Double direction = new Point2D.Double(point.x-ballCenter.x, point.y-ballCenter.y);
        double magnitudeDir = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        Point2D.Double unitDirection = new Point2D.Double(direction.x / magnitudeDir, direction.y / magnitudeDir);
        Point2D.Double v = ball.getVelocity();
        double dot = v.x * unitDirection.x + v.y * unitDirection.y;
        if (dot > 0) {
            Point2D.Double projection = new Point2D.Double(dot * unitDirection.x, dot*unitDirection.y);
            Point2D.Double perpendicular = new Point2D.Double(v.x - projection.x, v.y-projection.y);
            Point2D.Double vNew = new Point2D.Double(perpendicular.x - projection.x, perpendicular.y - projection.y);
            ball.setVelocity(vNew);
        }
    }

    public SimpleEntry<Block, Point2D.Double> firstCollisionPoint(Ball ball) {
        // This method returns immediately upon finding a colliding block. Its return is the block
        // with which the ball collides, and the point where they collide (like a tuple in python).
        double minCollisionDistance = ball.getRadius() + GameSettings.BLOCK_WIDTH/Math.sqrt(2) + GameSettings.EPS;
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
        // Each round we leave the ball in a state where it doesn't collide with anything. This way,
        // at the start of a new round, we don't need to check for collisions, only after moving.
        ball.move();
        handleWallCollision(ball);  // change direction from walls
        SimpleEntry<Block, Point2D.Double> blockAndPoint = firstCollisionPoint(ball);
        while (blockAndPoint !=  null) { // while any collisions are left
            Block block = blockAndPoint.getKey();
            Point2D.Double collisionPoint = blockAndPoint.getValue();
            bounceBallOffPoint(ball, collisionPoint); // reflect off a block
            block.decrementHealth(); // decrement HP, and remove if necessary
            // then move away from the block until the collision is resolved
            while (CollisionDetection.ballBlockCollisionPoint(ball, block) != null) {
                ball.move();
            }
            // then move on to the next colliding block
            blockAndPoint = firstCollisionPoint(ball);
        }
    }

    public void updateGameState(ActionEvent event) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (int i=0; i<physicsStepsPerTick; i++) {
            for (Ball ball: ballsInPlay) {
                updateBall(ball);
            }
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

    public GameStateSupervisor getGameStateSupervisor() {
        return gameStateSupervisor;
    }
}
