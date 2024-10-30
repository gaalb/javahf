package GBTAN;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private Game game;
    private javax.swing.Timer displayTimer;

    public GameFrame(Game game) {
        displayTimer = new Timer(1000/GameSettings.FPS, this::display);
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
