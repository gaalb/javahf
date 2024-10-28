package engine;

import model.*;
import model.GameData.GameState;
import display.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GameEngine {
    private GameData gameData;
    private GameFrame gameFrame;
    private javax.swing.Timer timer;
    private AimHandler aimHandler;

    public GameEngine() {
        gameData = new GameData(this);
        gameFrame = new GameFrame(this);
        aimHandler = new AimHandler(this);
        timer = new Timer(1000 / GameSettings.FPS, null);
        timer.start();
        timer.addActionListener(this::updateGameState); // todo: probably not like this
    }

    public void updateGameState(ActionEvent event) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (Ball ball: ballsInPlay) {
            ball.update();
        }
        gameFrame.repaint();
    }

    public Timer getTimer() {
        return timer;
    }

    public GameData getGameData() {
        return gameData;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void changeGameState(GameState newState) {
        gameData.setGameState(newState);
        if (newState == GameState.AIMING) {
            gameFrame.getGamePanel().addMouseListener(aimHandler);
            gameFrame.getGamePanel().addMouseMotionListener(aimHandler);

        }
        if (newState == GameState.PLAYING) {
            gameFrame.getGamePanel().removeMouseListener(aimHandler);
            gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
        }
    }
}
