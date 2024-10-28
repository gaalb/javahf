import engine.GameEngine;
import model.GameData;
import model.GameData.GameState;
import model.Block.BlockConfig;
import model.Block.BlockType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<BlockConfig> blocks = new ArrayList<>();
        blocks.add(new BlockConfig(0, 0, BlockType.TRIANGLE_UPPER_LEFT));
        blocks.add(new BlockConfig(1, 0, BlockType.TRIANGLE_UPPER_RIGHT));
        blocks.add(new BlockConfig(4, 5, BlockType.SQUARE));
        blocks.add(new BlockConfig(4, 4, BlockType.TRIANGLE_LOWER_RIGHT));
        blocks.add(new BlockConfig(0, 5, BlockType.SQUARE));
        blocks.add(new BlockConfig(6, 1, BlockType.TRIANGLE_LOWER_LEFT));
        blocks.add(new BlockConfig(3, 2, BlockType.SQUARE));
        blocks.add(new BlockConfig(2, 7, BlockType.SQUARE));
        SwingUtilities.invokeLater(()-> {
            GameEngine engine = new GameEngine();
            engine.getGameData().initializeGame(4, blocks, 200);
        });
    }
}
