package v1;

import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private GameData gameData;
    static final Dimension SCREEN_SIZE = new Dimension(500, 900);

    public GamePanel(GameData gameData) {
        this.gameData = gameData;
        this.setDoubleBuffered(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Ball ball: gameData.getBalls()) {
            BallDisplay.draw(g, ball);
        }
    }


}
