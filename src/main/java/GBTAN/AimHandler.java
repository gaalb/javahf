package GBTAN;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class AimHandler extends MouseAdapter {
    private Game game;
    private boolean isAiming;
    public AimHandler(Game game) {
        this.isAiming = false;
        this.game = game;
    }

    public void updateAimingAngle(Point p) {
        Cannon cannon = game.getGameData().getCannon();
        Point2D.Double cannonPosition = cannon.getPosition();
        double dx = p.getX() - cannonPosition.getX();
        double dy = cannonPosition.getY() - p.getY();
        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);
        cannon.setAimAngle(Math.max(30, Math.min(150, angleDeg)));
        game.getGameFrame().getGamePanel().repaint();
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
