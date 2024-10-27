package v2.View;

import v2.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

public class GamePanel extends JPanel {
    private Dimension dimension;
    private GameData gameData;


    public GamePanel(GameData gameData) {
        this.gameData = gameData;
        this.setDoubleBuffered(true);
        dimension = new Dimension(GameData.GAME_WIDTH, GameData.GAME_HEIGHT);
        this.setPreferredSize(dimension);
    }

    private void paintBackground(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintObjectSpots(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        // Loop through each spot in the grid and draw a small dot at the center
        for (int row = 0; row < GameData.BLOCK_ROWS; row++) {
            for (int col = 0; col < GameData.BLOCK_COLUMNS; col++) {
                ObjectSpot spot = gameData.getSpots()[row][col];
                Point2D.Double center = spot.getCenter();

                int dotRadius = 6;
                int dotX = (int) (center.x - 0.5* dotRadius);
                int dotY = (int) (center.y - 0.5* dotRadius);
                g2d.fillOval(dotX, dotY, dotRadius, dotRadius);
            }
        }
    }

    private void paintCannon(Graphics2D g2d) {
        Cannon cannon = gameData.getPlayer().getCannon();
        g2d.setColor(Color.RED);
        int circleRadius = 10;
        int triangleHeight = 40;
        int triangleWidth = 20;
        double x = cannon.getPosition().getX();
        double y = cannon.getPosition().getY();
        double angleDeg = cannon.getAimAngle()-90;
        double angle = Math.toRadians(angleDeg);
        g2d.fillOval((int)x-circleRadius, (int)y-circleRadius, circleRadius*2, circleRadius*2);

        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x, y-0.5*circleRadius);
        g2d.rotate(-angle);

        int[] xPoints = {
                0,
                -triangleWidth / 2,
                triangleWidth / 2
        };
        int[] yPoints = {
                +circleRadius/2,
                -triangleHeight+circleRadius,
                -triangleHeight+circleRadius
        };
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setTransform(originalTransform);
    }

    private void paintBalls(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (Ball b: ballsInPlay) {
            int x = (int)(b.getPosition().x - b.getRadius());
            int y = (int)(b.getPosition().y - b.getRadius());
            int w = (int)(2*b.getRadius());
            int h = (int)(2*b.getRadius());
            g2d.fillOval(x, y, w, h);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d);
        paintObjectSpots(g2d);
        paintCannon(g2d);
        paintBalls(g2d);
    }

}
