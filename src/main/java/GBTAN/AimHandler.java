package GBTAN;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Handles aiming functionality for the game by capturing mouse events.
 * Updates the cannon's aim angle based on mouse movement and triggers firing.
 */
public class AimHandler extends MouseAdapter {

    /**
     * The game instance this handler interacts with.
     */
    private final Game game;

    /**
     * Tracks whether the player is currently aiming (mouse button is pressed).
     */
    private boolean isAiming;

    /**
     * Creates an AimHandler for managing the aiming logic.
     *
     * @param game the game instance associated with this AimHandler.
     */
    public AimHandler(Game game) {
        this.isAiming = false; // toggles when mouse button is pressed, toggles back when it's released
        this.game = game;
    }

    /**
     * Updates the cannon's aiming angle based on the given mouse position.
     * Ensures the angle remains within the allowed bounds.
     *
     * @param p the current position of the mouse pointer.
     * @see Cannon#setAimAngle(double)
     * @see GameSettings#MIN_AIM_ANGLE
     */
    public void updateAimingAngle(Point p) {
        Cannon cannon = game.getGameData().getCannon();
        Point2D.Double cannonPosition = cannon.getPosition();
        double dx = p.getX() - cannonPosition.getX(); // x distance between mouse and cannon
        double dy = cannonPosition.getY() - p.getY(); // y distance between mouse and cannon
        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);
        // cannon degrees are measured in normal person coordinates, where x points right, y points up
        cannon.setAimAngle(Math.max(GameSettings.MIN_AIM_ANGLE, Math.min(180 - GameSettings.MIN_AIM_ANGLE, angleDeg)));
        game.getGameFrame().getGamePanel().repaint(); // cannon angle modified: refresh look
    }

    /**
     * Handles mouse press events to start aiming.
     * Triggers aiming mode and adjusts the cannon angle based on the initial pointer position.
     *
     * @param event the mouse press event.
     * @see #updateAimingAngle(Point)
     */
    @Override
    public void mousePressed(MouseEvent event) {
        isAiming = true;
        updateAimingAngle(event.getPoint());
    }

    /**
     * Handles mouse release events to stop aiming and fire the cannon.
     * This ends aiming mode and instructs the cannon to fire all stored balls.
     *
     * @param event the mouse release event.
     * @see Cannon#fireAll()
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        if (isAiming) {
            isAiming = false;
            game.getGameData().getCannon().fireAll();
        }
    }

    /**
     * Handles mouse drag events to update the aiming angle dynamically.
     * Only updates the aim if the player is actively aiming.
     *
     * @param event the mouse drag event.
     * @see #updateAimingAngle(Point)
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        if (isAiming) {
            updateAimingAngle(event.getPoint());
        }
    }
}
