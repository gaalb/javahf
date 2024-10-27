package v1;

import javax.swing.*;

public class GameDisplay extends JFrame {
    private GamePanel gamePanel;

    public GameDisplay(GameData gameData) {
        initialize(gameData);
    }

    private void initialize(GameData gameData) {
        gamePanel = new GamePanel(gameData);
        this.add(gamePanel);
        this.setTitle("GBTAN");
        this.setSize(500, 900);  // same ratio as my phone;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void addInputHandler(InputHandler inputHandler) {
        this.gamePanel.addMouseListener(inputHandler);
    }
}
