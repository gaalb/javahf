package GBTAN;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import GBTAN.GameData.GameState;

public class Game {
    private GameData gameData;
    private GameFrame gameFrame;
    private PhysicsEngine physicsEngine;
    private AimHandler aimHandler;

    public Game() {
        gameData = new GameData(this);
        gameFrame = new GameFrame(this);
        physicsEngine = new PhysicsEngine(this);
        physicsEngine.getPhysicsTimer().addActionListener(this::checkGameState);
        aimHandler = new AimHandler(this);
    }

    public GameData getGameData() {
        return gameData;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    private void setPlaying() {
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().removeMouseMotionListener(aimHandler);
    }

    private void setGameOver() {
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().removeMouseMotionListener(aimHandler);
        physicsEngine.stopPhysics();
        JButton newGameButton = gameFrame.getGamePanel().getNewGameButton();
        newGameButton.addActionListener(e-> {
            gameData.initializeGame(GameSettings.DEFAULT_CONFIG());
        });
    }

    private void setAiming() {
        gameFrame.getGamePanel().addMouseListener(aimHandler);
        gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
        physicsEngine.startPhysics();
        Cannon cannon = gameData.getCannon();
        List<Ball> balls = gameData.getBalls();
        for (Ball b: balls) {
            if (b.getState() == Ball.BallState.RETURNED) {
                cannon.storeBall(b);
            }
        }
    }

    public void setGameState(GameState newState) {
        gameData.setGameState(newState);
        if (newState == GameState.PLAYING) {
            setPlaying();
        }
        if (newState == GameState.AIMING) {
            setAiming();
        }
        if (newState == GameState.GAME_OVER) {
            setGameOver();
        }
    }

    private void checkGameState(ActionEvent event) {
        // check conditions for changing game state
        switch (gameData.getGameState()) {
            case GameState.PLAYING:
                if (gameData.getBallsInPlay().isEmpty()) {
                    setGameState(GameState.AIMING);
                    ObjectSpot[][] spots = gameData.getSpots();
                    for (int i = spots.length-1; i > 0; i--) {
                        ObjectSpot[] upperRow = spots[i-1];
                        ObjectSpot[] lowerRow = spots[i];
                        for (int j=0; j<lowerRow.length; j++) {
                            if (upperRow[j].getObject() != null) {
                                lowerRow[j].setObject(upperRow[j].getObject());
                                upperRow[j].clearObject();
                            }
                        }
                    }
                }
                break;
            case GameState.AIMING:
                ObjectSpot[] lastRow = gameData.getSpots()[GameSettings.BLOCK_ROWS-1];
                for (ObjectSpot spot: lastRow) {
                    if (spot.getObject() != null) {
                        setGameState(GameState.GAME_OVER);
                        return;
                    }
                }
                if (!gameData.getBallsInPlay().isEmpty()) {
                    setGameState(GameState.PLAYING);
                }
                break;
            case GameState.GAME_OVER:
                break;
            default:
                break;
        }
    }
}
