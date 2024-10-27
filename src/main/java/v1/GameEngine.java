package v1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class GameEngine {
    private GameData gameData;
    private GameDisplay gameDisplay;
    private javax.swing.Timer timer;
    private final int FPS = 100;
    private final int DELAY = 1000/FPS;

    public GameEngine() {
        gameData = new GameData();
        gameDisplay = new GameDisplay(gameData);
        gameDisplay.addInputHandler(new InputHandler(this, gameDisplay.getGamePanel()));
    }

    public void updateGameData(ActionEvent e) {
        gameData.updateGameState();
    }

    public void startGameLoop() {
        timer = new Timer(DELAY, (event)->{
            updateGameData(event);
            gameDisplay.repaint();  // repaints the game panel as well
        });
        timer.start();
    }
    public void fireCannon() {
        gameData.getBalls().add(new Ball(new Point(400, 600), 90));
    }

    public void updateAimingAngle(double angle) {

    }
}
