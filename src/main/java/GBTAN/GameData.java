package GBTAN;

import GBTAN.Ball.BallState;
import GBTAN.GameSave.SpotConfig;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the underlying data of the game, managing game objects, the cannon, balls,
 * and the current state of the game.
 */
public class GameData {

    /**
     * Enumerates the possible states of the game.
     */
    public enum GameState {
        PLAYING, // Balls are flying, cannon disabled
        AIMING, // Waiting for the player to fire
        GAME_OVER // Waiting to start new game
    }

    /**
     * A 2D array of object spots representing the game grid.
     * Each spot can hold a {@link CollideableObject}, such as a block or boon.
     */
    private final ObjectSpot[][] spots;

    /**
     * A list of all balls in the game, including those in play, in the cannon, or returned.
     */
    private final List<Ball> balls;

    /**
     * The current game instance.
     */
    private final Game game;

    /**
     * The current state of the game.
     */
    private GameState gameState;

    /**
     * The cannon used by the player to fire balls.
     */
    private Cannon cannon;

    /**
     * Initializes the game data, including the game grid, balls, and cannon.
     *
     * @param game The game instance associated with this data.
     */
    public GameData(Game game) {
        this.game = game;
        spots = new ObjectSpot[GameSettings.BLOCK_ROWS][GameSettings.BLOCK_COLUMNS];
        initializeSpots();
        balls = new LinkedList<>();  // contains all Balls, whether IN_STORE, IN_PLAY or RETURNED
    }

    /**
     * Retrieves all blocks currently present on the grid.
     *
     * @return A list of blocks in the game.
     * @see Block
     */
    public List<Block> getBlocks() {
        // Some CollideableObjects in objects are Blocks, some are Boons: return the Blocks.
        List<Block> blocks = new LinkedList<>();
        for (ObjectSpot[] row : spots) {
            for (ObjectSpot spot : row) {
                if (spot.getObject() instanceof Block) blocks.add((Block) spot.getObject());
            }
        }
        return blocks;
    }

    /**
     * Retrieves all boons currently present on the grid.
     *
     * @return A list of boons in the game.
     * @see Boon
     */
    public List<Boon> getBoons() {
        // Some CollideableObjects in objects are Blocks, some are Boons: return the Boons.
        List<Boon> boons = new LinkedList<>();
        for (ObjectSpot[] row : spots) {
            for (ObjectSpot spot : row) {
                if (spot.getObject() instanceof Boon) boons.add((Boon) spot.getObject());
            }
        }
        return boons;
    }

    /**
     * Initializes the grid of object spots for the game.
     * Each spot represents a location on the grid and is centered appropriately.
     */
    private void initializeSpots() {
        for (int row = 0; row < GameSettings.BLOCK_ROWS; row++) {
            for (int col = 0; col < GameSettings.BLOCK_COLUMNS; col++) {
                double centerX = (col * GameSettings.BLOCK_WIDTH) + (GameSettings.BLOCK_WIDTH / 2.0);
                double centerY = (row * GameSettings.BLOCK_HEIGHT) + (GameSettings.BLOCK_HEIGHT / 2.0);
                Point2D.Double center = new Point2D.Double(centerX, centerY);
                spots[row][col] = new ObjectSpot(center, GameSettings.BLOCK_WIDTH);
            }
        }
    }

    /**
     * Retrieves the cannon associated with the game.
     *
     * @return The cannon instance.
     * @see Cannon
     */
    public Cannon getCannon() {
        return cannon;
    }

    /**
     * Sets the cannon for the game.
     *
     * @param cannon The cannon to set.
     */
    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    /**
     * Retrieves the grid of object spots.
     *
     * @return A 2D array of object spots.
     * @see ObjectSpot
     */
    public ObjectSpot[][] getSpots() {
        return spots;
    }

    /**
     * Retrieves all balls currently in the game.
     *
     * @return A list of all balls.
     * @see Ball
     */
    public List<Ball> getBalls() {
        return balls;
    }

    /**
     * Retrieves all balls currently in play.
     *
     * @return A list of balls in play.
     */
    public List<Ball> getBallsInPlay() {
        List<Ball> ballsInPlay = new LinkedList<>();
        for (Ball ball : balls) {
            if (ball.getState() == BallState.IN_PLAY) {
                ballsInPlay.add(ball);
            }
        }
        return ballsInPlay;
    }

    /**
     * Retrieves all balls that have been returned to the cannon.
     *
     * @return A list of returned balls.
     */
    public List<Ball> getBallsReturned() {
        List<Ball> ballsReturned = new LinkedList<>();
        for (Ball ball : balls) {
            if (ball.getState() == BallState.RETURNED) {
                ballsReturned.add(ball);
            }
        }
        return ballsReturned;
    }

    /**
     * Gets the current state of the game.
     *
     * @return The game state.
     * @see GameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the current state of the game.
     *
     * @param gameState The new game state.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Clears all objects from the grid, resetting the game board.
     */
    public void clearObjects() {
        // clear the board: all spots should store null objects
        for (ObjectSpot[] row: spots) {
            for (ObjectSpot spot: row) {
                if (spot.getObject() != null) {
                    destroyObject(spot.getObject());
                }
            }
        }
    }

    /**
     * Assigns a game object to a specific spot on the grid.
     *
     * @param o    The object to assign.
     * @param spot The spot to assign the object to.
     * @see CollideableObject
     */
    public void assignObjectToSpot(CollideableObject o, ObjectSpot spot) {
        if (spot.getObject() != null) destroyObject(spot.getObject());
        spot.setObject(o);
    }

    /**
     * Destroys a game object by removing it from the grid.
     *
     * @param object The object to destroy.
     * @see CollideableObject
     */
    public void destroyObject(CollideableObject object) {
        object.getSpot().clearObject(); // destroy the reference to the Object in the Spot
        object.setSpot(null); // destroy the reference to the Spot in the Object
    }

    /**
     * Initializes the game data from a saved game state.
     *
     * @param gameSave The saved game state to load.
     * @see GameSave
     */
    public void initialize(GameSave gameSave) {
        clearObjects();
        balls.clear();
        for (int y = 0; y < gameSave.spots.length; y++) {
            for (int x = 0; x < gameSave.spots[y].length; x++) {
                SpotConfig config = gameSave.spots[y][x];
                ObjectSpot spot = spots[y][x];
                CollideableObject obj;
                switch (config.objectType) {
                    case NULL:
                        spot.setObject(null);
                        break;
                    case PLUS_ONE:
                        obj = new PlusOne(GameSettings.BOON_RADIUS, game);
                        assignObjectToSpot(obj, spot);
                        break;
                    case RANDOMIZER:
                        obj = new Randomizer(GameSettings.BOON_RADIUS, game);
                        assignObjectToSpot(obj, spot);
                        break;
                    default: // default = Block
                        obj = new Block(config.objectType, config.hp, game);
                        assignObjectToSpot(obj, spot);
                        break;
                }
            }
        }
        cannon = new Cannon(gameSave.cannonPos, 90, game);
        for (int i = 0; i < gameSave.ballNum; i++) {
            Ball ball = new Ball(cannon.getPosition(), new Point2D.Double(0, 0), GameSettings.BALL_RADIUS, BallState.IN_STORE, game);
            balls.add(ball);
            cannon.storeBall(ball);
        }
    }
}
