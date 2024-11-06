import GBTAN.Game;
import GBTAN.GameSettings;
import GBTAN.GameConfig;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            Game game = new Game();
            GameConfig c = new GameConfig(new File("defaultConfig.json"));
            game.initializeGame(c);
        });
    }
}
