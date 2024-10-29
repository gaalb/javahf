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
    public static final int GAME_DIAMETER = (int)Math.sqrt(Math.pow(GAME_HEIGHT, 2) + Math.pow(GAME_WIDTH, 2));
    public static final int FPS = 100;
    public static final int PHYSICS_FREQ = 2000;
    public static final double BALL_SPEED = 0.7;
    public static final int STARTING_BALL_NUM = 4;
    public static final int FRAMES_BETWEEN_BALLS = 8;
    public static final double EPS = 0.1;
}
