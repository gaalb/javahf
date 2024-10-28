package display;

import engine.AimHandler;
import engine.GameEngine;
import model.Ball;
import model.Cannon;
import model.GameData;
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

    public void setGameState(GameState newState) {
        gameData.setGameState(newState);
        if (newState == GameState.PLAYING) {
            setPlaying();
        }
        if (newState == GameState.AIMING) {
            setAiming();
        }
    }

    public void check(ActionEvent ae) {
        if (gameData.getGameState() == GameState.PLAYING) {
            if (gameData.getBallsInPlay().isEmpty()) {
                setGameState(GameState.AIMING);
            }
        } else if (gameData.getGameState() == GameState.AIMING) {
            if (!gameData.getBallsInPlay().isEmpty()) {
                setGameState(GameState.PLAYING);
            }
        }

    }

}
