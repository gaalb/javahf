package GBTAN;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    Game game;
    public TopPanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GameSettings.GAME_WIDTH, GameSettings.BLOCK_HEIGHT/2));
    }

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
        g2d.drawString(score, middleX, metrics.getAscent());
        String name = game.getPlayer().getName();
        g2d.drawString(name, GameSettings.BLOCK_WIDTH/2, metrics.getAscent());
        String prog3 = "ProgAlap 3";
        int prog3Width = metrics.stringWidth(prog3);
        g2d.drawString(prog3, getWidth()-GameSettings.BLOCK_WIDTH/2-prog3Width, metrics.getAscent());
    }
}
