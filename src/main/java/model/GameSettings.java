package model;

import model.Block.BlockConfig;
import model.Cannon.CannonConfig;
import model.GameData.GameConfig;
import model.Block.BlockType;
import model.GameSettings;

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
    public static final int PHYSICS_FREQ = 2000;
    public static final double BALL_SPEED = 0.7;
    public static final int STARTING_BALL_NUM = 4;
    public static final int FRAMES_BETWEEN_BALLS = 8;
    public static final double EPS = 0.1;
    public static GameConfig DEFAULT_CONFIG() {
        List<BlockConfig> blocks = new ArrayList<>();
        blocks.add(new BlockConfig(0, 0, 10, BlockType.TRIANGLE_UPPER_LEFT));
        blocks.add(new BlockConfig(1, 0, 9, BlockType.TRIANGLE_UPPER_RIGHT));
        blocks.add(new BlockConfig(4, 5, 8, BlockType.SQUARE));
        blocks.add(new BlockConfig(4, 4, 7, BlockType.TRIANGLE_LOWER_RIGHT));
        blocks.add(new BlockConfig(0, 5, 6, BlockType.SQUARE));
        blocks.add(new BlockConfig(6, 1, 5, BlockType.TRIANGLE_LOWER_LEFT));
        blocks.add(new BlockConfig(3, 2, 4, BlockType.SQUARE));
        blocks.add(new BlockConfig(2, 7, 1,BlockType.SQUARE));
        CannonConfig cannonConfig = new CannonConfig(200, 90, GameSettings.STARTING_BALL_NUM);
        return new GameConfig(cannonConfig, blocks);
    }
}
