package GBTAN;

import GBTAN.Ball.BallState;
import GBTAN.GameSave.SpotConfig;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;


public class GameData {
    public enum GameState {
        PLAYING, AIMING, GAME_OVER;
    }
    private final ObjectSpot[][] spots;  // ObjectSpots are static for the whole game duration, hence array not list
    private final List<CollideableObject> objects;
    private final List<Ball> balls;
    private final Game game;
    private GameState gameState;
    private Cannon cannon;

    public GameData(Game game) {
        this.game = game;;
        spots = new ObjectSpot[GameSettings.BLOCK_ROWS][GameSettings.BLOCK_COLUMNS];
        initializeSpots();
        objects = new LinkedList<>();  // contains all CollideableObjects: Blocks or Boons
        balls = new LinkedList<>();  // contains all Balls: IN_STORE, IN_PLAY or RETURNED
    }

    public List<CollideableObject> getObjects() {
        return objects;
    }

    public List<Block> getBlocks() {
        // Some CollideableObjects in objects are Blocks, some are Boons: return the Blocks.
        List<Block> blocks = new LinkedList<>();
        for (CollideableObject object: objects) {
            if (object instanceof Block) {
                blocks.add((Block)object);
            }
        }
        return blocks;
    }

    public List<Boon> getBoons() {
        // Some CollideableObjects in objects are Blocks, some are Boons: return the Boons.
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
        objects.clear();
        for (ObjectSpot[] row: spots) {
            for (ObjectSpot spot: row) {
                if (spot.getObject() != null) {
                    destroyObject(spot.getObject());
                }
            }
        }
    }

    public void assignObjectToSpot(CollideableObject o, ObjectSpot spot) {
        if (spot.getObject() != null) destroyObject(spot.getObject());
        spot.setObject(o);
        this.objects.add(o);
    }

    public void destroyObject(CollideableObject object) {
        object.getSpot().clearObject(); // destroy the reference to the Object in the Spot
        object.setSpot(null); // destroy the reference to the Spot in the Object
        objects.remove(object); // remove the reference to the Object from the GameData
    }

    public void initialize(GameSave gameSave) {
        clearObjects();
        balls.clear();
        for (int y = 0; y< gameSave.spots.length; y++) {
            for (int x = 0; x< gameSave.spots[y].length; x++) {
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
                    default:
                        obj = new Block(config.objectType, config.hp, game);
                        assignObjectToSpot(obj, spot);
                        break;
                }
            }
        }
        cannon = new Cannon(gameSave.cannonPos, 90, game);
        for (int i = 0; i< gameSave.ballNum; i++) {
            Ball ball = new Ball(cannon.getPosition(), new Point2D.Double(0, 0), GameSettings.BALL_RADIUS, BallState.IN_STORE, game);
            balls.add(ball);
            cannon.storeBall(ball);
        }
    }
}
