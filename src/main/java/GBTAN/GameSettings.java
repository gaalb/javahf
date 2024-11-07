package GBTAN;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameSettings {
    // Defaults and globals
    public static double BALL_RADIUS = 7;
    public static final int BLOCK_WIDTH = 60;
    public static final int BLOCK_HEIGHT = 60;
    public static final int BLOCK_COLUMNS = 7;
    public static final int BLOCK_ROWS = 9;
    public static final int GAME_WIDTH = BLOCK_WIDTH*BLOCK_COLUMNS;
    public static final int GAME_HEIGHT = BLOCK_HEIGHT*BLOCK_ROWS;
    public static final int GAME_DIAMETER = (int)Math.sqrt(Math.pow(GAME_HEIGHT, 2) + Math.pow(GAME_WIDTH, 2));
    public static final int FPS = 100;
    public static final int PHYSICS_TIMER_FREQ = 200;
    public static final int PHYSICS_FREQ = 1000;
    public static final double BALL_SPEED = 1.5;
    public static final int STARTING_BALL_NUM = 4;
    public static final int FRAMES_BETWEEN_BALLS = 8;
    public static final double EPS = 0.1;
    public static final double MIN_AIM_ANGLE = 20;
    public static final double BOON_RADIUS = 13;
    public static final File SAVES_FOLDER = new File("saves");
    public static final File PLAYERS_FOLDER = new File("players");
    public static final File DEFAULT_SAVE_FILE = new File(SAVES_FOLDER, "defaultSave.json");
    public static final File DEFAULT_PLAYER_FILE = new File(PLAYERS_FOLDER, "defaultPlayer.json");
}
