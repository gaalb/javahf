package GBTAN;

import GBTAN.Ball.BallState;
import GBTAN.Block.BlockConfig;
import GBTAN.Cannon.CannonConfig;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;


public class GameData {
    public enum GameState {
        PLAYING, AIMING, GAME_OVER;
    }

    public static class GameConfig {
        public CannonConfig cannonConfig;
        public List<BlockConfig> blockConfigs;
        public GameConfig(CannonConfig cannonConfig, List<BlockConfig> blockConfigs) {
            this.cannonConfig = cannonConfig;
            this.blockConfigs = blockConfigs;
        }
    }

    private final Player player;
    private final ObjectSpot[][] spots;
    private final List<CollideableObject> objects;
    private final List<Ball> balls;
    private final Game game;
    private GameState gameState;
    private Cannon cannon;

    public GameData(Game game) {
        this.game = game;
        player = new Player("GBotond", game);
        spots = new ObjectSpot[GameSettings.BLOCK_ROWS][GameSettings.BLOCK_COLUMNS];
        initializeSpots();
        objects = new LinkedList<>();
        balls = new LinkedList<>();
    }

    public List<CollideableObject> getObjects() {
        return objects;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new LinkedList<>();
        for (CollideableObject object: objects) {
            if (object instanceof Block) {
                blocks.add((Block)object);
            }
        }
        return blocks;
    }

    public List<Boon> getBoons() {
        List<Boon> boons = new LinkedList<>();
        for (CollideableObject object: objects) {
            if (object instanceof Boon) {
                boons.add((Boon)object);
            }
        }
        return boons;
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

    public List<Ball> getBallsReturned() {
        List<Ball> ballsReturned = new LinkedList<>();
        for (Ball ball: balls) {
            if (ball.getState() == BallState.RETURNED) {
                ballsReturned.add(ball);
            }
        }
        return ballsReturned;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void clearObjects() {
        for (ObjectSpot[] row: spots) {
            for (ObjectSpot spot: row) {
                if (spot.getObject() != null) {
                    destroyObject(spot.getObject());
                }
            }
        }
    }

    public void addBlock(BlockConfig config) {
        Block block = new Block(config.type, spots[config.y][config.x], config.hp, game);
        // the block and the spot have mutual references
        spots[config.y][config.x].setObject(block);
        this.objects.add(block);
    }

    public void destroyObject(CollideableObject object) {
        object.getSpot().clearObject();
        object.setSpot(null);
        objects.remove(object);
    }

    public void initializeGame(GameConfig gameConfig) {
        clearObjects();
        System.out.println("Cleared objects.");
        balls.clear();
        for (BlockConfig config: gameConfig.blockConfigs) {
            addBlock(config);
        }
        cannon = new Cannon(gameConfig.cannonConfig.x, gameConfig.cannonConfig.angle, game);
        for (int i=0; i<gameConfig.cannonConfig.ballNum; i++) {
            Point2D.Double p = new Point2D.Double(0.5*GameSettings.GAME_WIDTH, GameSettings.GAME_HEIGHT);
            Point2D.Double v = new Point2D.Double(0,0);
            Ball b = new Ball(p, v, GameSettings.BALL_RADIUS, Ball.BallState.IN_STORE, game);
            balls.add(b);
            cannon.storeBall(b);
        }
        game.setGameState(GameState.AIMING);
    }
}
