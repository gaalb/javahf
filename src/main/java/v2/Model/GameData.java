package v2.Model;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class GameData {
    // Defaults and semi-globals
    public static double BALL_RADIUS = 6;
    public static final int BLOCK_WIDTH = 60;
    public static final int BLOCK_HEIGHT = 60;
    public static final int BLOCK_COLUMNS = 7;
    public static final int BLOCK_ROWS = 8;
    public static final int GAME_WIDTH = BLOCK_WIDTH*BLOCK_COLUMNS;
    public static final int GAME_HEIGHT = BLOCK_HEIGHT*(BLOCK_ROWS+1);
    public static final int FPS = 100;
    public static final double BALL_SPEED = 500.0;

    private Player player;
    private ObjectSpot[][] spots;
    private List<Block> blocks;
    private List<Ball> ballsInPlay;
    public GameData() {
        player = new Player("GBotond");
        spots = new ObjectSpot[BLOCK_ROWS][BLOCK_COLUMNS];
        initializeSpots();
        blocks = new LinkedList<>();
        ballsInPlay = new LinkedList<>();
    }

    private void initializeSpots() {
        for (int row=0; row < BLOCK_ROWS; row++) {
            for (int col=0; col < BLOCK_COLUMNS; col++) {
                double centerX = (col * BLOCK_WIDTH) + (BLOCK_WIDTH / 2.0);
                double centerY = (row * BLOCK_HEIGHT) + (BLOCK_HEIGHT / 2.0);
                Point2D.Double center = new Point2D.Double(centerX, centerY);

                spots[row][col] = new ObjectSpot(center, BLOCK_WIDTH);
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public ObjectSpot[][] getSpots() {
        return spots;
    }

    public List<Ball> getBallsInPlay() {
        return ballsInPlay;
    }
}
