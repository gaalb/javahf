package GBTAN;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;

import GBTAN.GameData.GameState;
import GBTAN.CollideableObject.ObjectType;

/**
 * Manages the core gameplay loop, including game state transitions, round management,
 * and interactions between game components such as the physics engine, GUI, and player data.
 */
public class Game {

    /**
     * Stores and manages the state of the game, including objects on the grid and game state transitions.
     */
    private final GameData gameData;

    /**
     * The graphical user interface for the game, including buttons and the game panel.
     */
    private final GameFrame gameFrame;

    /**
     * Manages physics, such as ball movement and collisions.
     */
    private final PhysicsEngine physicsEngine;

    /**
     * Handles aiming based on player input.
     */
    private final AimHandler aimHandler;

    /**
     * Random number generator for gameplay elements such as spawning objects.
     */
    private final Random rng;

    /**
     * The player associated with the game, storing configuration and statistics.
     */
    private final Player player;

    /**
     * The current score of the game, representing the number of rounds survived.
     */
    private int score;

    /**
     * Initializes a new game instance with a player and a saved game state.
     *
     * @param player   the player associated with this game.
     * @param gameSave the saved game state to load.
     * @see GameSave
     * @see GameData
     */
    public Game(Player player, GameSave gameSave) {
        this.player = player;
        this.score = gameSave.score;
        gameData = new GameData(this);
        gameFrame = new GameFrame(this);
        physicsEngine = new PhysicsEngine(this);
        physicsEngine.getPhysicsTimer().addActionListener(this::checkGameState);
        aimHandler = new AimHandler(this);
        rng = new Random();

        gameFrame.getEndRoundButton().addActionListener(e -> {
            for (Ball ball: gameData.getBallsInPlay()) {
                gameData.getCannon().returnBall(ball);  // rounds can be ended by returning all balls
            }
        });

        gameFrame.getSpeedUpButton().addActionListener(e -> {
            physicsEngine.doubleSpeed();
            gameFrame.getSpeedUpButton().setEnabled(false);
        });

        JButton newGameButton = gameFrame.getGamePanel().getNewGameButton();
        newGameButton.addActionListener(e -> newGame());

        gameFrame.addWindowListener(new Disposer(this));

        gameData.initialize(gameSave);
        setGameState(GameState.AIMING);
    }

    /**
     * Gets the player associated with the game.
     *
     * @return the player instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current score of the game.
     *
     * @return the score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the game data object, which tracks objects on the grid and manages game state transitions.
     *
     * @return the game data.
     * @see GameData
     */
    public GameData getGameData() {
        return gameData;
    }

    /**
     * Gets the graphical interface for the game.
     *
     * @return the game frame.
     * @see GameFrame
     */
    public GameFrame getGameFrame() {
        return gameFrame;
    }

    /**
     * Gets the physics engine for the game.
     *
     * @return the physics engine.
     * @see PhysicsEngine
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    /**
     * Configures the game components when entering the "playing" state.
     * The player cannot aim, and physics are activated to move the balls.
     */
    private void setPlaying() {
        // When transitioning into "playing", the balls are moving, but the player cannot aim, meaning we that
        // the aim handler (which turns the cannon) must be disabled
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().removeMouseMotionListener(aimHandler);
        gameFrame.getEndRoundButton().setEnabled(true);
        gameFrame.getSpeedUpButton().setEnabled(true);
        physicsEngine.resetSpeed();
    }

    /**
     * Configures the game components when entering the "game over" state.
     * All interactions are disabled, and physics are stopped.
     */
    private void setGameOver() {
        // When the game is over, the player cannot aim -> disable aim handler
        gameFrame.getGamePanel().removeMouseListener(aimHandler);
        gameFrame.getGamePanel().removeMouseMotionListener(aimHandler);
        gameFrame.getEndRoundButton().setEnabled(false);
        gameFrame.getSpeedUpButton().setEnabled(false);
        physicsEngine.resetSpeed();
        physicsEngine.stopPhysics();  // the game is over, so physics (bounces, movement) must be disabled
    }

    /**
     * Configures the game components when entering the "aiming" state.
     * Enables the aim handler for mouse inputs and prepares the balls for firing.
     */
    private void setAiming() {
        // When the game is in its aiming stage, the aim handler must listen for mouse inputs
        gameFrame.getGamePanel().addMouseListener(aimHandler);
        gameFrame.getGamePanel().addMouseMotionListener(aimHandler);
        gameFrame.getEndRoundButton().setEnabled(false);
        gameFrame.getSpeedUpButton().setEnabled(false);
        physicsEngine.resetSpeed();
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

    /**
     * Sets the current state of the game and updates components accordingly.
     *
     * @param newState the new game state.
     * @see GameState
     */
    public void setGameState(GameState newState) {
        gameData.setGameState(newState);
        if (newState == GameState.PLAYING) {
            setPlaying();
        } else if (newState == GameState.AIMING) {
            setAiming();
        } else if (newState == GameState.GAME_OVER) {
            setGameOver();
        }
    }

    /**
     * Removes spent boons from the game grid and handles specific actions for certain boons,
     * such as adding balls for {@link PlusOne}.
     */
    private void cleanUpSpentBoons() {
        for (ObjectSpot[] row : gameData.getSpots()) {
            for (ObjectSpot spot : row) {
                CollideableObject obj = spot.getObject();
                if (obj instanceof Boon && ((Boon) obj).isSpent()) {
                    if (obj instanceof PlusOne) {
                        Ball newBall = new Ball(new Point2D.Double(0, 0), new Point2D.Double(0, 0),
                                GameSettings.BALL_RADIUS, Ball.BallState.RETURNED, this);
                        gameData.getBalls().add(newBall);
                    }
                    gameData.destroyObject(obj);
                }
            }
        }
    }

    /**
     * Moves all objects on the grid one row down, clearing the top row for new objects.
     */
    private void shiftObjects() {
        ObjectSpot[][] spots = gameData.getSpots();
        // Starting at the last row, and moving up (the other way around wouldn't work because
        // by the time we get to the Nth row, its object would be overwritten by the N-1th row.
        for (int i = spots.length-1; i > 0; i--) {
            ObjectSpot[] upperRow = spots[i-1];
            ObjectSpot[] lowerRow = spots[i];
            for (int j = 0; j < lowerRow.length; j++) {
                lowerRow[j].setObject(upperRow[j].getObject());
                upperRow[j].clearObject();
            }
        }
    }

    /**
     * Randomly selects an element from a map of weights, with higher weights increasing the selection probability.
     *
     * @param weights a map of elements and their corresponding weights.
     * @param <T>     the type of elements in the map.
     * @return a randomly selected element.
     */
    private <T> T randomChoice(Map<T, Double> weights) {
        // Returns an element randomly chosen from the dictionary's keys
        // The higher the value corresponding to the key, the higher the chance it gets chosen (relative chances).
        double totalWeight = 0.0;
        for (Double weigth: weights.values()) {totalWeight += weigth;} // sum of relative chances
        double randomNumber = rng.nextDouble()*totalWeight;
        // compare the random number to the rolling sum of relative chances: when we surpass it, return
        for (Map.Entry<T, Double> entry: weights.entrySet()) {
            randomNumber -= entry.getValue();
            if (randomNumber <= 0.0) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Spawns a new row of objects at the top of the grid, including blocks and boons,
     * based on the player's configuration.
     */
    private void spawnRow() {
        // Populate the topmost row with objects, according to the settings of the player
        // We do this by filling a list with the possible objects, then filling it with nulls until it's
        // the correct size, then shuffling it
        List<CollideableObject> objects = new LinkedList<>();
        objects.add(new PlusOne(GameSettings.BOON_RADIUS, this));  // spawn a PlusOne each round
        Integer blockNum = randomChoice(player.getBlockNumChance());  // RNG determines the spawned block count
        for (int i=0; i<blockNum; i++) {
            ObjectType type = randomChoice(player.getBlockTypeChance());  // RNG determines block type
            int hp = rng.nextDouble() <= player.getDoubleHPChance()? 2*score : score;  // RNG: double hp or not
            Block block = new Block(type, hp, this);
            objects.add(block);
        }
        // If we have space left for a randomizer, RNG may decide to spawn one
        if (objects.size() < GameSettings.BLOCK_COLUMNS && rng.nextDouble() <= player.getRandomizerChance()) {
            objects.add(new Randomizer(GameSettings.BOON_RADIUS, this));
        }
        // Fill with nulls, which will result in empty spots
        while (objects.size() < GameSettings.BLOCK_COLUMNS) {
            objects.add(null);
        }
        // Shuffle then place the generated objects
        Collections.shuffle(objects);
        ObjectSpot[] topRow = gameData.getSpots()[0];
        for (int i = 0; i < topRow.length; i++) {
            gameData.assignObjectToSpot(objects.get(i), topRow[i]);
        }
    }

    /**
     * Initializes a new game, resetting the game state and saving the player's high score if applicable.
     */
    public void newGame() {
        // A new game is where we initialize the game with an empty spot list
        if (score > player.getHighScore()) { // Before starting the new game, save the possible high score
            player.setHighScore(score);
            File playerFile = new File(GameSettings.PLAYERS_FOLDER, player.getName() + ".json");
            player.saveToFile(playerFile);
            System.out.println("HIGH SCORE!");
        }
        GameSave newGameSave = new GameSave(GameSettings.DEFAULT_SAVE_FILE);
        gameData.initialize(newGameSave);
        score = newGameSave.score;
        setGameState(GameState.AIMING);
    }

    /**
     * Advances the game to a new round by cleaning up spent boons, shifting objects, updating the score,
     * and spawning a new row of objects.
     */
    private void newRound() {
        cleanUpSpentBoons(); // delete used boons
        shiftObjects(); // push objects down a row
        score++; // indicate we survived a round
        spawnRow(); // populate topmost row, which is empty now that we pushed all rows down
        setGameState(GameState.AIMING);
    }

    /**
     * Monitors the game state during each physics tick and transitions between states if conditions are met.
     *
     * @param event the action event triggered by the physics timer.
     */
    private void checkGameState(ActionEvent event) {
        // Gets called once per physics loop: checks if the game's state must be updated
        switch (gameData.getGameState()) {
            case GameState.PLAYING:
                // When the round is playing (balls are moving, aiming is off), we must switch back to
                // aiming if all the balls go out of play
                if (gameData.getBallsInPlay().isEmpty()) {
                    newRound();
                }
                break;
            case GameState.AIMING:
                // When we start the aiming phase of the game, if the last row isn't empty, it means the
                // game is over (blocks are touching the bottom of the screen).
                ObjectSpot[] lastRow = gameData.getSpots()[GameSettings.BLOCK_ROWS - 1];
                for (ObjectSpot spot : lastRow) {
                    if (spot.getObject() instanceof Block) {
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
        }
    }
}
