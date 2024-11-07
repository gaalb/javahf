package GBTAN;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import GBTAN.GameData.GameState;

public class GamePanel extends JPanel {
    private final Dimension dimension;
    private final Game game;
    private final GameData gameData;
    private JButton newGameButton;

    public GamePanel(Game game) {
        this.game = game;
        this.gameData = game.getGameData();
        this.setDoubleBuffered(true);
        dimension = new Dimension(GameSettings.GAME_WIDTH, GameSettings.GAME_HEIGHT);
        this.setPreferredSize(dimension);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setupNewGameButton();
    }

    public void setupNewGameButton() {
        // The newGameButton is always present in memory, but is hidden and inactive normally
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
        for (int row = 0; row < GameSettings.BLOCK_ROWS-1; row++) { // last row has no dots
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
        // Paints the projected path of the balls as calculated by Cannon
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

        // Paint the Barrel of the Cannon, which is a triangle pointing away from the aimAngle,
        // hence the rotation, and its tip being ad the Cannon's location, hence the translation
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(-angle);
        int[] xPoints = {0, -triangleWidth / 2, triangleWidth / 2};
        int[] yPoints = {circleRadius,-triangleHeight+circleRadius,-triangleHeight+circleRadius};
        g2d.setColor(Color.RED);
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 3);
        // Delete the transformation we just used, to avoid messing with the other drawing methods.
        g2d.setTransform(originalTransform);
        g2d.setColor(Color.RED);
        g2d.fillOval((int)x-circleRadius, (int)y-circleRadius, circleRadius*2, circleRadius*2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int)x-circleRadius, (int)y-circleRadius, circleRadius*2, circleRadius*2);

        // Display number of balls
        g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
        String ballText = "x" + cannon.storedNumber();
        g2d.setColor(Color.BLACK);
        g2d.drawString(ballText, (int)x+circleRadius+2, dimension.height);
        g2d.setColor(Color.PINK);
        g2d.drawString(ballText, (int)x+circleRadius, dimension.height-2);
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

    private void paintBlock(Block block, Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18)); // Font for the HP
        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(block.getPolygon()); // body
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(block.getPolygon()); // outline
        String healthText = String.valueOf(block.getHealth()); // HP
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(healthText);
        int textHeight = fm.getAscent();
        g2d.setColor(Color.YELLOW);
        Point2D.Double position;
        // Depending on the type of the block, we must place the text in a different position to look nice
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

    private void paintBoon(Boon boon, Graphics2D g2d)  {
        int r = (int)boon.getRadius();
        int x = (int)boon.getPosition().x;
        int y = (int)boon.getPosition().y;
        if (boon instanceof PlusOne) {
            g2d.setColor(Color.CYAN);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawOval(x-r, y-r, 2*r, 2*r);
            g2d.drawLine(x, y-r+2, x, y+r-2);
            g2d.drawLine(x-r+2, y, x+r-2, y);
        } else if (boon instanceof Randomizer) {
            g2d.setColor(Color.MAGENTA);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawOval(x-r, y-r, 2*r, 2*r);
            AffineTransform originalTransform = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.PI/4);
            g2d.drawLine(0, 2-r, 0, r-2);
            g2d.drawLine(2-r, 0, r-2, 0);
            g2d.setTransform(originalTransform);
        }
    }

    private void paintObjects(Graphics2D g2d) {
        ObjectSpot[][] spots = gameData.getSpots();
        for (ObjectSpot[] row: spots) {
            for (ObjectSpot spot: row) {
                CollideableObject obj = spot.getObject();
                if (obj instanceof Block) paintBlock((Block)obj, g2d);
                else if (obj instanceof Boon && !((Boon)obj).isSpent()) paintBoon((Boon) obj, g2d);
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
        g2d.drawString(message, x + 3, y + 3); // Draw shadow for the text (looks nice)
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
        paintObjects(g2d);
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
