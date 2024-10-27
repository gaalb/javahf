package v1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InputHandler extends MouseAdapter {
    private GameEngine gameEngine;
    private boolean isAiming;
    private JPanel gamePanel;
    public InputHandler(GameEngine gameEngine, JPanel gamePanel) {
        this.gameEngine = gameEngine;
        this.gamePanel = gamePanel;
        isAiming = false;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        isAiming = true;
        updateAimingAngle(event.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (isAiming) {
            updateAimingAngle(event.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (isAiming) {
            isAiming = false;
            gameEngine.fireCannon();
        }

    }

    private void updateAimingAngle(Point mousePos) {

    }


}
