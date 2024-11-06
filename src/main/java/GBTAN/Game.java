package GBTAN;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

import GBTAN.GameData.GameState;
import GBTAN.CollideableObject.ObjectType;

public class Game {
    // Class that oversees the transition between game states and rounds.
    private final GameData gameData;
    private final GameFrame gameFrame;
    private final PhysicsEngine physicsEngine;
    private final AimHandler aimHandler;
    private final Random rng;

    public Game() {
        gameData = new GameData(new Player(new File("defaultPlayer.json")), this);
        gameFrame = new GameFrame(this);
        physicsEngine = new PhysicsEngine(this);
        physicsEngine.getPhysicsTimer().addActionListener(this::checkGameState);
        aimHandler = new AimHandler(this);
        rng = new Random();

        gameFrame.getEndRoundButton().addActionListener(e -> {
            for (Ball ball: gameData.getBallsInPlay()) {
                gameData.getCannon().returnBall(ball);
            }
        });
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

    public void initializeGame(GameConfig gameConfig) {
        gameData.initialize(gameConfig);
        spawnRow();
        setGameState(GameState.AIMING);
    }

    private void setPlaying() {
        // When transitioning into "playing", the balls are moving, but the player cannot aim, meaning we that
        // the aim handler (which turns the cannon) must be disabled
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().removeMouseMotionListener(aimHandler);
        gameFrame.getEndRoundButton().setEnabled(true);
    }

    private void setGameOver() {
        // When the game is over, the player cannot aim -> disable aim handler
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().removeMouseMotionListener(aimHandler);
        gameFrame.getEndRoundButton().setEnabled(false);
        physicsEngine.stopPhysics();  // the game is over, so physics (bounces, movement) must be disabled
        // We start paying attention to the "New Game" button: pressing it initializes a new game
        JButton newGameButton = gameFrame.getGamePanel().getNewGameButton();
        newGameButton.addActionListener(e-> {
            initializeGame(new GameConfig(new File("defaultConfig.json")));
        });
    }

    private void setAiming() {
        // When the game is in its aiming stage, the aim handler must listen for mouse inputs
        gameFrame.getGamePanel().addMouseListener(aimHandler);
        gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
        gameFrame.getEndRoundButton().setEnabled(false);
        // Physics are started, so that when the first ball emerges from the cannon, its position is updated
        physicsEngine.startPhysics();
        Cannon cannon = gameData.getCannon();
        List<Ball> balls = gameData.getBalls();
        for (Ball b: balls) { // Balls that exit the play area get stored in the cannon again
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

    private void shiftObjects() {
        // Moves all blocks present one row down, except the last row (since they have nowhere to go).
        ObjectSpot[][] spots = gameData.getSpots();
        // Starting at the last row, and moving up (the other way around wouldn't work because
        // by the time we get to the Nth row, its object would be overwritten by the N-1th row.
        for (int i = spots.length-1; i > 0; i--) {
            ObjectSpot[] upperRow = spots[i-1];
            ObjectSpot[] lowerRow = spots[i];
            for (int j=0; j<lowerRow.length; j++) {
                CollideableObject o = upperRow[j].getObject();
                if (o != null) {
                    if (o instanceof Boon && ((Boon)o).isSpent()) {
                        // spent boons get deleted instead of shifted
                        gameData.destroyObject(o);
                    } else {
                        lowerRow[j].setObject(upperRow[j].getObject());
                        upperRow[j].clearObject();
                    }
                }
            }
        }
    }

    private <T> T randomChoice(Map<T, Double> weights) {
        double totalWeight = 0.0;
        for (Double weigth: weights.values()) {totalWeight += weigth;}
        double randomNumber = rng.nextDouble()*totalWeight;
        for (Map.Entry<T, Double> entry: weights.entrySet()) {
            randomNumber -= entry.getValue();
            if (randomNumber <= 0.0) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void spawnRow() {
        Player player = gameData.getPlayer();
        List<CollideableObject> objects = new LinkedList<>();
        objects.add(new PlusOne(GameSettings.BOON_RADIUS, this));
        Integer blockNum = randomChoice(player.getBlockNumChance());
        for (int i=0; i<blockNum; i++) {
            ObjectType type = randomChoice(player.getBlockTypeChance());
            int hp = rng.nextDouble() <= player.getDoubleHPChance()? 2*gameData.getScore() : gameData.getScore();
            Block block = new Block(type, hp, this);
            objects.add(block);
        }
        if (objects.size() < GameSettings.BLOCK_COLUMNS && rng.nextDouble() <= player.getRandomizerChance()) {
            objects.add(new Randomizer(GameSettings.BOON_RADIUS, this));
        }
        while (objects.size() < GameSettings.BLOCK_COLUMNS) {
            objects.add(null);
        }
        Collections.shuffle(objects);
        ObjectSpot[] topRow = gameData.getSpots()[0];
        for (int i=0; i< topRow.length; i++) {
            gameData.assignObjectToSpot(objects.get(i), topRow[i]);
        }
    }

    private void checkGameState(ActionEvent event) {
        // Gets called once per physics loop: checks if the game's state must be updated
        switch (gameData.getGameState()) {
            case GameState.PLAYING:
                // When the round is playing (balls are moving, aiming is off), we must switch back to
                // aiming if all the balls go out of play
                if (gameData.getBallsInPlay().isEmpty()) {
                    setGameState(GameState.AIMING);
                    shiftObjects();
                    spawnRow();
                    gameData.setScore(gameData.getScore()+1);
                }
                break;
            case GameState.AIMING:
                // When we start the aiming phase of the game, if the last row isn't empty, it means the
                // game is over (blocks are touching the bottom of the screen).
                ObjectSpot[] lastRow = gameData.getSpots()[GameSettings.BLOCK_ROWS-1];
                for (ObjectSpot spot: lastRow) {
                    if (spot.getObject() != null && (spot.getObject() instanceof Block)) {
                        setGameState(GameState.GAME_OVER);
                        return;
                    }
                }
                // When we are aiming, if a ball enters the play area, aiming is over and playing starts.
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
