package v1;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            GameEngine engine = new GameEngine();
            engine.startGameLoop();
        });

    }
}
