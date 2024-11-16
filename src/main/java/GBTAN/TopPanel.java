package GBTAN;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the top panel of the game UI, which displays the player's name, current score,
 * and high score. It is styled with a black background and white text.
 */
public class TopPanel extends JPanel {
    /**
     * The game instance associated with this top panel.
     */
    private final Game game;

    /**
     * Constructs a new instance of this class associated with the specified game instance.
     *
     * @param game The game instance to retrieve score and player information from.
     */
    public TopPanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GameSettings.GAME_WIDTH, GameSettings.BLOCK_HEIGHT / 2));
    }

    /**
     * Paints the component, displaying the player's name, current score, and high score.
     * The player's name is displayed on the left, the score in the center, and the high score on the right.
     *
     * @param g The {@code Graphics} object to use for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        Font font = new Font("SansSerif", Font.BOLD, 22);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        String score = String.valueOf(game.getScore());
        int scoreWidth = metrics.stringWidth(score);
        int middleX = (getWidth()-scoreWidth)/2;
        // Score in the middle
        g2d.drawString(score, middleX, metrics.getAscent());
        String name = game.getPlayer().getName();
        // Name to the left
        g2d.drawString(name, 20, metrics.getAscent());
        String highScore = "High Score: "+game.getPlayer().getHighScore();
        int highScoreWidth = metrics.stringWidth(highScore);
        // High score to the right
        g2d.drawString(highScore, getWidth()-20-highScoreWidth, metrics.getAscent());
    }
}
