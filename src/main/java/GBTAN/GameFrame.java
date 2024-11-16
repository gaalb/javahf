package GBTAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Represents the main game frame that contains all visual elements of the game,
 * including the game panel, top panel, and bottom panel with buttons.
 */
public class GameFrame extends JFrame {

    /**
     * The panel displaying the game board.
     */
    private final GamePanel gamePanel;

    /**
     * The main game instance associated with this frame.
     */
    private final Game game;

    /**
     * Timer for repainting the frame at a constant frame rate.
     */
    private final Timer displayTimer;

    /**
     * Button for ending the current round.
     */
    private final JButton endRoundButton;

    /**
     * Button for doubling the game speed.
     */
    private final JButton speedUpButton;

    /**
     * The panel at the bottom of the frame, housing action buttons.
     */
    private final JPanel bottomPanel;

    /**
     * The panel at the top of the frame, displaying the score and player information.
     */
    private final TopPanel topPanel;

    /**
     * Constructs the game frame, initializes its layout and components, and starts the display timer.
     *
     * @param game The main game instance associated with this frame.
     */
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
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, GameSettings.BLOCK_WIDTH, GameSettings.BLOCK_WIDTH/10));
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

    /**
     * Repaints the game frame, updating the visual elements.
     *
     * @param e The action event triggered by the display timer.
     */
    public void display(ActionEvent e) {
        repaint();
    }

    /**
     * Retrieves the game panel displaying the game board.
     *
     * @return The {@link GamePanel} instance.
     * @see GamePanel
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /**
     * Retrieves the button for ending the current round.
     *
     * @return The "End Round" button.
     */
    public JButton getEndRoundButton() {
        return endRoundButton;
    }

    /**
     * Retrieves the button for doubling the game speed.
     *
     * @return The "2x Speed" button.
     */
    public JButton getSpeedUpButton() {
        return speedUpButton;
    }
}
