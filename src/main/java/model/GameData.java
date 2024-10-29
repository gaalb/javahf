package model;

import engine.GameEngine;
import model.Ball.BallState;
import model.Block.BlockType;
import model.Block.BlockConfig;

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
        balls = new LinkedList<>();
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

    public void initializeGame(int numberOfBalls, List<BlockConfig> blocks, int cannonPlacement) {
        gameEngine.getGameStateSupervisor().setGameState(GameState.AIMING);
        for (BlockConfig config: blocks) {
            Block block = new Block(config.type, spots[config.y][config.x], 10, gameEngine);
            // the block and the spot have mutual references
            spots[config.y][config.x].setObject(block);
            this.blocks.add(block);

        }
        cannon = new Cannon(cannonPlacement, 90, gameEngine);
        for (int i=0; i<numberOfBalls; i++) {
            Point2D.Double p = new Point2D.Double(0.5*GameSettings.GAME_WIDTH, GameSettings.GAME_HEIGHT);
            Point2D.Double v = new Point2D.Double(0,0);
            Ball b = new Ball(p, v, GameSettings.BALL_RADIUS, Ball.BallState.IN_STORE, gameEngine);
            balls.add(b);
            cannon.storeBall(b);
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

    public List<Ball> getBallsReturned() {
        List<Ball> ballsReturned = new LinkedList<>();
        for (Ball ball: balls) {
            if (ball.getState() == BallState.RETURNED) {
                ballsReturned.add(ball);
            }
        }
        return ballsReturned;
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

    public List<Block> getBlocks() {
        return blocks;
    }
}
