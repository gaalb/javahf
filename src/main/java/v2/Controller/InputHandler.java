package v2.Controller;

import v2.GameEngine;
import v2.Model.*;
import v2.View.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler extends MouseAdapter {
    private GameFrame gameFrame;
    private GameData gameData;
    private GameEngine gameEngine;
    private boolean isAiming;
    public InputHandler(GameFrame gameFrame, GameData gameData, GameEngine gameEngine) {
        this.isAiming = false;
        this.gameFrame = gameFrame;
        this.gameData = gameData;
        this.gameEngine = gameEngine;
    }

    public void updateAimingAngle(Point p) {
        Cannon cannon = gameData.getPlayer().getCannon();
        Point cannonPosition = cannon.getPosition();
        double dx = p.getX() - cannonPosition.getX();
        double dy = cannonPosition.getY() - p.getY();
        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);
        cannon.setAimAngle(Math.max(30, Math.min(150, angleDeg)));
        gameFrame.getGamePanel().repaint();
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
            gameData.getPlayer().getCannon().fireAll(gameData, gameEngine.getTimer());
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (isAiming) {
            updateAimingAngle(event.getPoint());
        }
    }

}
