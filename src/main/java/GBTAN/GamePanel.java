package GBTAN;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import GBTAN.GameData.GameState;

public class GamePanel extends JPanel {
    private Dimension dimension;
    private Game game;
    private GameData gameData;
    private JButton newGameButton;

    public GamePanel(Game game) {
        this.game = game;
        this.gameData = game.getGameData();
        this.setDoubleBuffered(true);
        dimension = new Dimension(GameSettings.GAME_WIDTH, GameSettings.GAME_HEIGHT);
        this.setPreferredSize(dimension);
        // Setup layour and newGameButton, which is always present in memory, just not always visualized
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        newGameButton = new JButton("NEW GAME");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 24));
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setVisible(false);
        add(Box.createVerticalGlue());
        add(newGameButton);
        add(Box.createVerticalGlue());
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    private void paintBackground(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintObjectSpots(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        // Loop through each spot in the grid and draw a small dot at the center
        for (int row = 0; row < GameSettings.BLOCK_ROWS-1; row++) {
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

    private void paintAimAssist(Graphics2D g2d) {
        Cannon cannon = gameData.getCannon();
        double x = cannon.getPosition().getX();
        double y = cannon.getPosition().getY();
        g2d.setColor(Color.GREEN);
        Point2D.Double aimAssistPoint = cannon.project();
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine((int)aimAssistPoint.x, (int)aimAssistPoint.y, (int)x, (int)y);
        int r = (int)GameSettings.BALL_RADIUS;
        g2d.fillOval((int)aimAssistPoint.x-r, (int)aimAssistPoint.y-r, 2*r, 2*r);
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
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18)); // Set a larger, bold font for the health text

        for (Block block : blocks) {
            g2d.setColor(Color.BLUE);
            g2d.fillPolygon(block.getPolygon());
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolygon(block.getPolygon());

            String healthText = String.valueOf(block.getHealth());
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(healthText);
            int textHeight = fm.getAscent();

            g2d.setColor(Color.YELLOW);

            Point2D.Double position;
            switch (block.getType()) {
                case SQUARE:
                    position = block.getPosition();
                    g2d.drawString(healthText, (int)position.x - textWidth / 2, (int)position.y + textHeight / 2);
                    break;
                case TRIANGLE_LOWER_LEFT:
                case TRIANGLE_LOWER_RIGHT:
                    position = new Point2D.Double(
                            (block.getCorners()[0].x + block.getCorners()[1].x + block.getCorners()[2].x) / 3,
                            (block.getCorners()[0].y + block.getCorners()[1].y + block.getCorners()[2].y) / 3
                    );
                    g2d.drawString(healthText, (int)position.x - textWidth / 2, (int)position.y + textHeight / 2);
                    break;
                case TRIANGLE_UPPER_LEFT:
                case TRIANGLE_UPPER_RIGHT:
                    position = new Point2D.Double(
                            (block.getCorners()[0].x + block.getCorners()[1].x + block.getCorners()[2].x) / 3,
                            (block.getCorners()[0].y + block.getCorners()[1].y + block.getCorners()[2].y) / 3
                    );
                    g2d.drawString(healthText, (int)position.x - textWidth / 2, (int)position.y);
                    break;
            }
        }
    }

    private void paintGameOver(Graphics2D g2d) {
        Font font = new Font("Arial", Font.BOLD, 48);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        String message = "GAME OVER";
        int x = (getWidth() - metrics.stringWidth(message)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2;
        g2d.setColor(Color.BLACK);
        g2d.drawString(message, x + 3, y + 3);
        g2d.setColor(Color.BLACK);
        g2d.drawString(message, x - 1, y);
        g2d.drawString(message, x + 1, y);
        g2d.drawString(message, x, y - 1);
        g2d.drawString(message, x, y + 1);
        g2d.setColor(Color.RED);
        g2d.drawString(message, x, y);
        newGameButton.setVisible(true);
    }

    private void paintPlaying(Graphics2D g2d) {
        paintBalls(g2d);
        paintCannon(g2d);
        newGameButton.setVisible(false);
    }

    private void paintAiming(Graphics2D g2d) {
        paintBalls(g2d);
        paintAimAssist(g2d);
        paintCannon(g2d);
        newGameButton.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d);
        paintObjectSpots(g2d);
        paintBlocks(g2d);
        switch (gameData.getGameState()) {
            case GameState.PLAYING:
                paintPlaying(g2d);
                break;
            case GameState.AIMING:
                paintAiming(g2d);
                break;
            case GameState.GAME_OVER:
                paintGameOver(g2d);
                break;
            default:
                break;
        }
    }
}
