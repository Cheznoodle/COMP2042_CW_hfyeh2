package brickGame.Controller;

import brickGame.Direction;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
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
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                mainClass.move(Direction.LEFT);
                break;
            case RIGHT:
                mainClass.move(Direction.RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
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
}
