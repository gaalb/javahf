package GBTAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameFrame extends JFrame {
    private final GamePanel gamePanel;
    private final Game game;
    private final javax.swing.Timer displayTimer;
    private final JButton endRoundButton;
    private final JButton speedUpButton;
    private final JPanel bottomPanel;
    private final TopPanel topPanel;

    public GameFrame(Game game) {
        // Timer that ticks with the desired FPS value, regardless of the physics rate.
        displayTimer = new Timer(1000/GameSettings.FPS, this::display); // paint frame on tick
        displayTimer.start();
        this.game = game;

        setLayout(new BorderLayout());
        gamePanel = new GamePanel(game);
        this.add(gamePanel, BorderLayout.CENTER);

        topPanel = new TopPanel(game);
        this.add(topPanel, BorderLayout.NORTH);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, GameSettings.BLOCK_WIDTH, GameSettings.BLOCK_WIDTH/10 ));
        endRoundButton = new JButton("End Round");
        speedUpButton = new JButton("2x Speed");
        bottomPanel.add(speedUpButton);
        bottomPanel.add(endRoundButton);
        bottomPanel.setBackground(Color.lightGray);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.setTitle("GBTAN");
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    public JButton getEndRoundButton() {
        return endRoundButton;
    }

    public JButton getSpeedUpButton() {
        return speedUpButton;
    }
}
