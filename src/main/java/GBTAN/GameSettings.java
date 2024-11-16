package GBTAN;

import java.io.File;

/**
 * Contains global constants and default settings for the game.
 */
public class GameSettings {
    /**
     * The default radius of a ball in the game.
     */
    public static double BALL_RADIUS = 7;

    /**
     * The width of a single block in pixels.
     */
    public static final int BLOCK_WIDTH = 60;

    /**
     * The height of a single block in pixels.
     */
    public static final int BLOCK_HEIGHT = 60;

    /**
     * The number of columns in the block grid.
     */
    public static final int BLOCK_COLUMNS = 7;

    /**
     * The number of rows in the block grid.
     */
    public static final int BLOCK_ROWS = 9;

    /**
     * The width of the game area in pixels, calculated from block dimensions.
     */
    public static final int GAME_WIDTH = BLOCK_WIDTH * BLOCK_COLUMNS;

    /**
     * The height of the game area in pixels, calculated from block dimensions.
     */
    public static final int GAME_HEIGHT = BLOCK_HEIGHT * BLOCK_ROWS;

    /**
     * The frames per second (FPS) for display rendering.
     */
    public static final int FPS = 100;

    /**
     * The frequency of the physics timer in milliseconds.
     */
    public static final int PHYSICS_TIMER_FREQ = 200;

    /**
     * The frequency of the physics updates in hertz.
     */
    public static final int PHYSICS_FREQ = 1000;

    /**
     * The default speed of a ball in the game.
     */
    public static final double BALL_SPEED = 1.5;

    /**
     * The number of frames between firing consecutive balls.
     */
    public static final int FRAMES_BETWEEN_BALLS = 8;

    /**
     * A small epsilon value used for numerical precision.
     */
    public static final double EPS = 0.1;

    /**
     * The minimum allowed aiming angle in degrees.
     */
    public static final double MIN_AIM_ANGLE = 20;

    /**
     * The radius of a boon object in the game.
     */
    public static final double BOON_RADIUS = 13;

    /**
     * The directory where save files are stored.
     */
    public static final File SAVES_FOLDER = new File("saves");

    /**
     * The directory where player profile files are stored.
     */
    public static final File PLAYERS_FOLDER = new File("players");

    /**
     * The default save file path.
     */
    public static final File DEFAULT_SAVE_FILE = new File(SAVES_FOLDER, "defaultSave.json");

    /**
     * The default player profile file path.
     */
    public static final File DEFAULT_PLAYER_FILE = new File(PLAYERS_FOLDER, "defaultPlayer.json");
}
