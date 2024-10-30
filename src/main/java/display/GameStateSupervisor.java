package display;

import engine.AimHandler;
import engine.GameEngine;
import model.*;
import model.GameData.GameState;

import java.awt.event.ActionEvent;
import java.util.List;

public class GameStateSupervisor {
    private GameEngine gameEngine;
    private GameData gameData;
    private GameFrame gameFrame;
    private AimHandler aimHandler;

    public GameStateSupervisor(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.gameData = gameEngine.getGameData();
        this.gameFrame = gameEngine.getGameFrame();
        this.gameEngine.getTimer().addActionListener(this::check);
        aimHandler = new AimHandler(gameEngine);
    }

    private void setAiming() {
        gameFrame.getGamePanel().addMouseListener(aimHandler);
        gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
        Cannon cannon = gameData.getCannon();
        List<Ball> balls = gameData.getBalls();
        for (Ball b: balls) {
            if (b.getState() == Ball.BallState.RETURNED) {
                cannon.storeBall(b);
            }
        }
    }

    private void setPlaying() {
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
    }

    private void setGameOver() {
        gameEngine.stopTimer();
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

    public void check(ActionEvent ae) {
        if (gameData.getGameState() == GameState.PLAYING) {
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
        } else if (gameData.getGameState() == GameState.AIMING) {
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
        }

    }

}
