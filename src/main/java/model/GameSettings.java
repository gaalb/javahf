package model;

public class GameSettings {
    // Defaults and globals
    public static double BALL_RADIUS = 7;
    public static final int BLOCK_WIDTH = 60;
    public static final int BLOCK_HEIGHT = 60;
    public static final int BLOCK_COLUMNS = 7;
    public static final int BLOCK_ROWS = 8;
    public static final int GAME_WIDTH = BLOCK_WIDTH*BLOCK_COLUMNS;
    public static final int GAME_HEIGHT = BLOCK_HEIGHT*(BLOCK_ROWS+1);
    public static final int FPS = 100;
    public static final int PHYSICS_STEP_MS = 1;
    public static final double BALL_SPEED = 7.0;
    public static final int STARTING_BALL_NUM = 4;
    public static final int FRAMES_BETWEEN_BALLS = 8;
    public static final double EPS = 0.1;
}
