package display;

import engine.GameEngine;

import javax.swing.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameEngine gameEngine;

    public GameFrame(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        gamePanel = new GamePanel(gameEngine);
        this.add(gamePanel);
        this.setTitle("GBTAN");
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

}
