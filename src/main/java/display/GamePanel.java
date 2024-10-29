package display;

import engine.GameEngine;
import model.GameSettings;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

public class GamePanel extends JPanel {
    private Dimension dimension;
    private GameEngine gameEngine;
    private GameData gameData;


    public GamePanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.gameData = gameEngine.getGameData();
        this.setDoubleBuffered(true);
        dimension = new Dimension(GameSettings.GAME_WIDTH, GameSettings.GAME_HEIGHT);
        this.setPreferredSize(dimension);
    }

    private void paintBackground(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintObjectSpots(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        // Loop through each spot in the grid and draw a small dot at the center
        for (int row = 0; row < GameSettings.BLOCK_ROWS; row++) {
            for (int col = 0; col < GameSettings.BLOCK_COLUMNS; col++) {
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
        Cannon cannon = gameData.getCannon();
        int circleRadius = 15;
        int triangleHeight = 60;
        int triangleWidth = 35;
        double x = cannon.getPosition().getX();
        double y = cannon.getPosition().getY();
        double angleDeg = cannon.getAimAngle()-90;
        double angle = Math.toRadians(angleDeg);

        g2d.setColor(Color.GREEN);
        Point2D.Double aimAssistPoint = cannon.project();
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine((int)aimAssistPoint.x, (int)aimAssistPoint.y, (int)x, (int)y);
        int r = (int)GameSettings.BALL_RADIUS;
        g2d.fillOval((int)aimAssistPoint.x-r, (int)aimAssistPoint.y-r, 2*r, 2*r);

        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(-angle);

        int[] xPoints = {
                0,
                -triangleWidth / 2,
                triangleWidth / 2
        };
        int[] yPoints = {
                circleRadius,
                -triangleHeight+circleRadius,
                -triangleHeight+circleRadius
        };
        g2d.setColor(Color.RED);
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 3);
        g2d.setTransform(originalTransform);

        g2d.setColor(Color.RED);
        g2d.fillOval((int)x-circleRadius, (int)y-circleRadius, circleRadius*2, circleRadius*2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int)x-circleRadius, (int)y-circleRadius, circleRadius*2, circleRadius*2);
    }

    private void paintBalls(Graphics2D g2d) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (Ball b: ballsInPlay) {
            int x = (int)(b.getPosition().x - b.getRadius());
            int y = (int)(b.getPosition().y - b.getRadius());
            int w = (int)(2*b.getRadius());
            int h = (int)(2*b.getRadius());
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x, y, w, h);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x, y, w, h);
        }
    }

    private void paintBlocks(Graphics2D g2d) {
        List<Block> blocks = gameData.getBlocks();
        for (Block block: blocks) {
            g2d.setColor(Color.BLUE);
            g2d.fillPolygon(block.getPolygon());
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolygon(block.getPolygon());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d);
        paintObjectSpots(g2d);
        paintBlocks(g2d);
        paintBalls(g2d);
        paintCannon(g2d);
    }

}
