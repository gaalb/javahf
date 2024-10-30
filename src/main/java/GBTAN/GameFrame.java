package GBTAN;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameFrame extends JFrame {
    private final GamePanel gamePanel;
    private final Game game;
    private final javax.swing.Timer displayTimer;

    public GameFrame(Game game) {
        // Timer that ticks with the desired FPS value, regardless of the physics rate.
        displayTimer = new Timer(1000/GameSettings.FPS, this::display); // paint frame on tick
        displayTimer.start();
        this.game = game;
        gamePanel = new GamePanel(game);
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
}
