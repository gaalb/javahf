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
        SwingUtilities.invokeLater(()-> {
            GameEngine engine = new GameEngine();
            engine.getGameData().initializeGame(4, blocks, 150);
            //engine.runGameLoop();
        });
    }
}
