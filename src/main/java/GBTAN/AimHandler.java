package GBTAN;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class AimHandler extends MouseAdapter {
    private final Game game;
    private boolean isAiming;
    public AimHandler(Game game) {
        this.isAiming = false; // toggles when mouse button is pressed, toggles back when it's released
        this.game = game;
    }

    public void updateAimingAngle(Point p) {
        Cannon cannon = game.getGameData().getCannon();
        Point2D.Double cannonPosition = cannon.getPosition();
        double dx = p.getX() - cannonPosition.getX(); // x distance between mouse and cannon
        double dy = cannonPosition.getY() - p.getY(); // y distance between mouse and cannon
        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);
        // cannon degrees are measured in normal person coordinates, where x points right, y points up
        cannon.setAimAngle(Math.max(GameSettings.MIN_AIM_ANGLE, Math.min(180-GameSettings.MIN_AIM_ANGLE, angleDeg)));
        game.getGameFrame().getGamePanel().repaint(); // cannon angle modified: refresh look
    }

    @Override
    public void mousePressed(MouseEvent event) {
        isAiming = true;
        updateAimingAngle(event.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (isAiming) {
            isAiming = false;
            game.getGameData().getCannon().fireAll();
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (isAiming) {
            updateAimingAngle(event.getPoint());
        }
    }

}
