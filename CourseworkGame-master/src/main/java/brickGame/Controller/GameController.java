package brickGame.Controller;

import brickGame.Direction;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.logging.Level;
import java.util.logging.Logger;
import brickGame.Main;

public class GameController implements EventHandler<KeyEvent> {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private Main mainClass;

    public GameController(Main main) {
        this.mainClass = main;
    }

    /**
     * Handles keyboard events for controlling the game.
     * Supports movement, game actions, and other controls based on key presses.
     *
     * @param event The keyboard event that occurred.
     */
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(Direction.LEFT); // Call move method in GameController
                break;
            case RIGHT:
                move(Direction.RIGHT); // Call move method in GameController
                break;
            case DOWN:
                // setPhysicsToBall();
                break;
            case S:
                mainClass.saveGame();
                break;
            case M:
                mainClass.toggleSound();
                break;
            case ESCAPE:
                mainClass.togglePause();
                break;
        }
    }

    /**
     * Moves the player's paddle in a specified direction.
     * Creates a new thread for the movement to provide smooth control.
     *
     * @param direction The direction to move the paddle (LEFT or RIGHT).
     */
    public void move(Direction direction) {
        new Thread(() -> {
            int sleepTime = 4;
            for (int i = 0; i < 30; i++) {
                if (direction == Direction.RIGHT) {
                    mainClass.updatePaddlePosition(1);
                } else if (direction == Direction.LEFT) {
                    mainClass.updatePaddlePosition(-1);
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Interrupted exception in move method", e);
                    Thread.currentThread().interrupt();
                }
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }
}
