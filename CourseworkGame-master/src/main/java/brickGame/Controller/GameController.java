package brickGame.Controller;

import brickGame.Direction;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.logging.Level;
import java.util.logging.Logger;
import brickGame.Main;

/**
 * This class serves as the controller for the game, handling user input through keyboard events.
 * It interacts with the Main class to update the game state based on the player's actions.
 */
public class GameController implements EventHandler<KeyEvent> {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private Main mainClass;

    /**
     * Constructs a GameController object.
     *
     * @param main The main class of the game, used to invoke game-related methods.
     */
    public GameController(Main main) {
        this.mainClass = main;
    }

    /**
     * Handles keyboard events for game controls.
     * Key actions include moving left/right, pausing the game, saving the game, and toggling sound.
     *
     * @param event The keyboard event triggered by the player's input.
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
                // setPhysicsToBall();
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
     * Initiates movement of the player's paddle in the specified direction.
     * The movement is handled in a separate thread to ensure smooth control.
     *
     * @param direction The direction in which the paddle should move (LEFT or RIGHT).
     */
    public void move(Direction direction) {
        new Thread(() -> {
            int sleepTime = 4; // Initial delay time between movement steps
            for (int i = 0; i < 30; i++) { // Loop to provide continuous movement
                if (direction == Direction.RIGHT) {
                    mainClass.updatePaddlePosition(1); // Move right
                } else if (direction == Direction.LEFT) {
                    mainClass.updatePaddlePosition(-1); // Move left
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
