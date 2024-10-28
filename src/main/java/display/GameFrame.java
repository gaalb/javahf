package display;

import engine.GameEngine;
import model.GameSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameEngine gameEngine;
    private javax.swing.Timer displayTimer;

    public GameFrame(GameEngine gameEngine) {
        displayTimer = new Timer(1000/GameSettings.FPS, this::display);
        displayTimer.start();
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

    public void display(ActionEvent e) {
        repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

}
