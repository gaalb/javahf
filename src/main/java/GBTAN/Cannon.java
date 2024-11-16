package GBTAN;

import GBTAN.Ball.BallState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

/**
 * Represents the cannon controlled by the player, used to fire and store balls.
 * The cannon can adjust its aim, fire balls sequentially, and handle ball storage.
 */
public class Cannon {

    /**
     * The angle at which the cannon is aimed, in degrees, measured by right hand rule, with 0 degrees to the right.
     */
    private double aimAngle;

    /**
     * The position of the cannon on the game field. Y is always the bottom of the gameplay panel.
     */
    private Point2D.Double position;

    /**
     * The game instance this cannon belongs to.
     */
    private final Game game;

    /**
     * A list of balls currently stored in the cannon.
     */
    private final List<Ball> storedBalls;

    /**
     * Creates a new cannon at the specified horizontal position and aim angle.
     *
     * @param xPosition the horizontal position of the cannon.
     * @param aimAngle  the initial aiming angle of the cannon, in degrees.
     * @param game      the game instance this cannon belongs to.
     */
    public Cannon(double xPosition, double aimAngle, Game game) {
        this.game = game;
        this.position = new Point2D.Double(xPosition, GameSettings.GAME_HEIGHT);
        this.aimAngle = aimAngle;
        this.storedBalls = new LinkedList<>();
    }

    /**
     * Gets the current aim angle of the cannon.
     *
     * @return the aim angle in degrees.
     */
    public double getAimAngle() {
        return aimAngle;
    }

    /**
     * Sets the aim angle of the cannon.
     *
     * @param aimAngle the new aim angle, in degrees.
     */
    public void setAimAngle(double aimAngle) {
        this.aimAngle = aimAngle;
    }

    /**
     * Gets the position of the cannon.
     *
     * @return a copy of the cannon's position.
     */
    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y);
    }

    /**
     * Sets the horizontal position of the cannon.
     *
     * @param xPosition the new horizontal position.
     */
    public void setPosition(double xPosition) {
        this.position = new Point2D.Double(xPosition, position.y);
    }

    /**
     * Sets the position of the cannon.
     *
     * @param x the x-coordinate of the new position.
     * @param y the y-coordinate of the new position.
     */
    public void setPosition(double x, double y) {
        this.position = new Point2D.Double(x, y);
    }

    /**
     * Gets the number of balls currently stored in the cannon.
     *
     * @return the number of stored balls.
     */
    public int storedNumber() {
        return storedBalls.size();
    }

    /**
     * Fires a single ball from the cannon.
     * The ball's velocity is determined by the cannon's aim angle and game settings.
     */
    public void fireSingleBall() {
        Ball b = storedBalls.removeFirst();
        b.setState(Ball.BallState.IN_PLAY);
        double vx = GameSettings.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
        double vy = -GameSettings.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
        b.setVelocity(new Point2D.Double(vx, vy));
    }

    /**
     * Fires all stored balls sequentially, with a delay between each shot.
     * Uses a {@link FireHandler} to manage the timing of the shots.
     */
    public void fireAll() {
        Timer timer = game.getPhysicsEngine().getPhysicsTimer();
        FireHandler fireHandler = new FireHandler(GameSettings.FRAMES_BETWEEN_BALLS, timer);
        timer.addActionListener(fireHandler);
    }

    /**
     * Stores a returned ball in the cannon.
     * The ball's position is reset to the cannon's position.
     *
     * @param ball the ball to store.
     * @see Ball.BallState#IN_STORE
     */
    public void storeBall(Ball ball) {
        storedBalls.add(ball);
        ball.setState(BallState.IN_STORE);
        ball.setPosition(position);
    }

    /**
     * Returns a ball to the cannon from play.
     * The cannon's position is updated to match the ball's position if it's the first returned ball.
     *
     * @param ball the ball being returned.
     * @see Ball.BallState#RETURNED
     */
    public void returnBall(Ball ball) {
        if (game.getGameData().getBallsReturned().isEmpty()) {
            setPosition(ball.getPosition().x);
        }
        ball.setState(BallState.RETURNED);
    }

    /**
     * Projects the trajectory of a ball fired from the cannon, determining
     * the first point of collision with walls or blocks.
     *
     * @return the projected collision point.
     * @see Ball
     * @see PhysicsEngine#firstCollisionPoint(Ball)
     */
    public Point2D.Double project() {
        Cannon cannon = game.getGameData().getCannon();
        double aimAngle = cannon.getAimAngle();
        double vx = GameSettings.BALL_SPEED * Math.cos(Math.toRadians(aimAngle));
        double vy = -GameSettings.BALL_SPEED * Math.sin(Math.toRadians(aimAngle));
        Ball ball = new Ball(cannon.getPosition(), new Point2D.Double(vx, vy), GameSettings.BALL_RADIUS, BallState.IN_PLAY, game);

        // Move the ball until a collision is registered
        while (true) {
            ball.move();
            if (ball.getPosition().x < ball.getRadius()) break;
            if (ball.getPosition().x > GameSettings.GAME_WIDTH - ball.getRadius()) break;
            if (ball.getPosition().y < ball.getRadius()) break;
            if (game.getPhysicsEngine().firstCollisionPoint(ball) != null) break;
        }
        return ball.getPosition();
    }

    /**
     * Handles the sequential firing of balls using a timer.
     * Fires a ball at intervals defined by {@link GameSettings#FRAMES_BETWEEN_BALLS}.
     */
    private class FireHandler implements ActionListener {

        /**
         * The counter storing the number of frames since last firing.
         */
        private int frameCounter;

        /**
         * How many frames should bass between shots.
         */
        private final int framesPerShot;

        /**
         * The timer for firing the balls.
         */
        private final Timer timer;

        /**
         * Creates a handler to manage sequential firing of balls.
         *
         * @param framesPerShot the number of frames between each shot.
         * @param timer          the timer managing the firing sequence.
         */
        public FireHandler(int framesPerShot, Timer timer) {
            this.frameCounter = framesPerShot;
            this.framesPerShot = framesPerShot;
            this.timer = timer;
        }

        /**
         * Handles firing a ball when the timer ticks and stops when the cannon is empty.
         *
         * @param e the action event triggered by the timer.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!storedBalls.isEmpty()) {
                if (frameCounter >= framesPerShot) {
                    fireSingleBall();
                    frameCounter = 0;
                }
                frameCounter++;
            } else {
                timer.removeActionListener(this);
            }
        }
    }
}
