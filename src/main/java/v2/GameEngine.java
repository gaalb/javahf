package v2;

import v2.Controller.InputHandler;
import v2.Model.*;
import v2.View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GameEngine {
    private GameData gameData;
    private GameFrame gameFrame;
    private javax.swing.Timer timer;

    public GameEngine() {
        gameData = new GameData();
        gameFrame = new GameFrame(gameData);
        InputHandler panelMouseHandler = new InputHandler(gameFrame, gameData, this);
        gameFrame.getGamePanel().addMouseListener(panelMouseHandler);
        gameFrame.getGamePanel().addMouseMotionListener(panelMouseHandler);
        timer = new Timer(1000/GameData.FPS, this::updateGameState);
        timer.start();
    }

    public void playRound() {

    }

    public void runGameLoop() {
    //    while (true) {
    //        playRound();
    //    }
    }

    public void updateGameState(ActionEvent event) {
        List<Ball> ballsInPlay = gameData.getBallsInPlay();
        for (Ball ball: ballsInPlay) {
            ball.update(gameData);
        }
        gameFrame.repaint();
    }

    public Timer getTimer() {
        return timer;
    }

}
