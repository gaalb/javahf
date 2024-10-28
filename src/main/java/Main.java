import engine.GameEngine;
import model.GameData;
import model.GameData.GameState;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            GameEngine engine = new GameEngine();
            engine.changeGameState(GameState.AIMING);
            //engine.runGameLoop();
        });
    }
}
