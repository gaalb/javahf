import engine.GameEngine;
import model.GameData;
import model.GameData.GameState;
import model.Block.BlockConfig;
import model.Cannon.CannonConfig;
import model.GameData.GameConfig;
import model.Block.BlockType;
import model.GameSettings;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            GameEngine engine = new GameEngine();
            engine.getGameData().initializeGame(GameSettings.DEFAULT_CONFIG());
        });
    }
}
