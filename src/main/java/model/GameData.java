package model;

import engine.GameEngine;
import model.Ball.BallState;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;


public class GameData {
    public enum GameState {
        PLAYING, AIMING, GAME_OVER;
    }

    private final Player player;
    private final ObjectSpot[][] spots;
    private final List<Block> blocks;
    private final List<Ball> balls;
    private final GameEngine gameEngine;
    private GameState gameState;
    private Cannon cannon;

    public GameData(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        player = new Player("GBotond", gameEngine);
        spots = new ObjectSpot[GameSettings.BLOCK_ROWS][GameSettings.BLOCK_COLUMNS];
        initializeSpots();
        blocks = new LinkedList<>();
        gameState = GameState.AIMING;

        balls = new LinkedList<>();
        for (int i=0; i<GameSettings.STARTING_BALL_NUM; i++) {
            Point2D.Double p = new Point2D.Double(0.5*GameSettings.GAME_WIDTH, GameSettings.GAME_HEIGHT);
            Point2D.Double v = new Point2D.Double(0,0);
            balls.add(new Ball(p, v, GameSettings.BALL_RADIUS, Ball.BallState.IN_STORE, gameEngine));
        }
        List<Ball> storedBalls = new LinkedList<>(balls);
        cannon = new Cannon(GameSettings.GAME_WIDTH / 2, 90, storedBalls,gameEngine);
    }

    private void initializeSpots() {
        for (int row=0; row < GameSettings.BLOCK_ROWS; row++) {
            for (int col=0; col < GameSettings.BLOCK_COLUMNS; col++) {
                double centerX = (col * GameSettings.BLOCK_WIDTH) + (GameSettings.BLOCK_WIDTH / 2.0);
                double centerY = (row * GameSettings.BLOCK_HEIGHT) + (GameSettings.BLOCK_HEIGHT / 2.0);
                Point2D.Double center = new Point2D.Double(centerX, centerY);

                spots[row][col] = new ObjectSpot(center, GameSettings.BLOCK_WIDTH);
            }
        }
    }

    public Cannon getCannon() {
        return cannon;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public ObjectSpot[][] getSpots() {
        return spots;
    }

    public List<Ball> getBalls() {
        return balls;
    }


    public List<Ball> getBallsInPlay() {
        List<Ball> ballsInPlay = new LinkedList<>();
        for (Ball ball: balls) {
            if (ball.getState() == BallState.IN_PLAY) {
                ballsInPlay.add(ball);
            }
        }
        return ballsInPlay;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
