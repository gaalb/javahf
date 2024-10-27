package v1;

import java.awt.*;
import java.util.*;


public class GameData {
    private Player player;
    private java.util.List<Ball> balls;

    public GameData() {
        player = new Player();
        balls = new ArrayList<>();
        balls.add(new Ball(new Point(GamePanel.SCREEN_SIZE.width, GamePanel.SCREEN_SIZE.height), 90));
    }

    public void updateGameState() {
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update();
            if (!ball.isActive()) {
                ballIterator.remove();
            }
        }
    }
    public java.util.List<Ball> getBalls() {
        return balls;
    }
}
