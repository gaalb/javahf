package v2.View;

import v2.Model.*;
import v2.Controller.*;

import javax.swing.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameData gameData;

    public GameFrame(GameData gameData) {
        this.gameData = gameData;
        gamePanel = new GamePanel(gameData);
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

}
