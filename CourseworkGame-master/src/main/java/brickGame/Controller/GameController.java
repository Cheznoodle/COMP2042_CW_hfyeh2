package brickGame.Controller;

import brickGame.Direction;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import brickGame.Model.Main;

/**
 * GameController handles keyboard inputs for the game and updates the game state accordingly.
 * It provides functionality to move the paddle, save the game state, toggle sound, and pause the game.
 */
public class GameController implements EventHandler<KeyEvent> {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private Main mainClass;

    /**
     * Constructor for GameController.
     *
     * @param main The main class instance of the game. This is used to invoke game-related methods.
     */
    public GameController(Main main) {
        this.mainClass = main;
    }

    /**
     * Handles key events generated from user input.
     * It processes input such as moving left/right, pausing the game, saving the game state, and toggling sound.
     *
     * @param event The keyboard event triggered by the player.
     */
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(Direction.LEFT);
                break;
            case RIGHT:
                move(Direction.RIGHT);
                break;
            case DOWN:

                break;
            case S:
                mainClass.saveGame(); // Save the current game state.
                break;
            case M:
                mainClass.toggleSound(); // Toggle the game sound on or off.
                break;
            case ESCAPE:
                mainClass.togglePause(); // Pause or resume the game.
                break;
        }
    }

    /**
     * Initiates the movement of the paddle based on the given direction.
     * The movement is executed in a separate thread to ensure responsive and smooth control.
     *
     * @param direction The desired direction for the paddle movement (LEFT or RIGHT).
     */
    public void move(Direction direction) {
        new Thread(() -> {
            int sleepTime = 4; // Initial delay time between movement steps
            for (int i = 0; i < 30; i++) { // Loop to provide continuous movement
                if (direction == Direction.RIGHT) {
                    mainClass.updatePaddlePosition(1); // Moves the paddle to the right.
                } else if (direction == Direction.LEFT) {
                    mainClass.updatePaddlePosition(-1); // Moves the paddle to the left.
                }
                try {
                    Thread.sleep(sleepTime); // Pause to control movement speed
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Interrupted exception in move method", e);
                    Thread.currentThread().interrupt(); // Properly handle thread interruption
                }
                if (i >= 20) {
                    sleepTime = i; // Adjust sleep time for smoother movement
                }
            }
        }).start(); // Start the thread for paddle movement
    }
}
