package engine;

import model.*;
import model.GameData.GameState;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class AimHandler extends MouseAdapter {
    private GameEngine gameEngine;
    private boolean isAiming;
    public AimHandler(GameEngine gameEngine) {
        this.isAiming = false;
        this.gameEngine = gameEngine;
    }

    public void updateAimingAngle(Point p) {
        Cannon cannon = gameEngine.getGameData().getCannon();
        Point2D.Double cannonPosition = cannon.getPosition();
        double dx = p.getX() - cannonPosition.getX();
        double dy = cannonPosition.getY() - p.getY();
        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);
        cannon.setAimAngle(Math.max(30, Math.min(150, angleDeg)));
        gameEngine.getGameFrame().getGamePanel().repaint();
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
            gameEngine.getGameData().getCannon().fireAll();
            gameEngine.changeGameState(GameState.PLAYING);
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (isAiming) {
            updateAimingAngle(event.getPoint());
        }
    }

}
