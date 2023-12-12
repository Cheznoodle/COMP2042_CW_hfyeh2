package brickGame.View;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * Provides utility functions for creating user interface elements in the game.
 * This class includes methods to construct and configure menus and buttons used within the game.
 */
public class UserInterface {

    /**
     * Initializes and returns a VBox containing the pause menu interface.
     * The pause menu includes options to resume the game or exit to the main menu.
     *
     * @param primaryStage      The primary stage of the JavaFX application, used to attach event handlers.
     * @param togglePauseAction The action to be performed when the "Resume Game" button is clicked.
     * @param exitAction        The action to be performed when the "Exit Game" button is clicked.
     * @return A VBox containing the pause menu elements.
     */
    public static VBox initializePauseMenu(Stage primaryStage, Runnable togglePauseAction, Runnable exitAction) {
        VBox pauseMenuVBox = new VBox(10);
        pauseMenuVBox.setAlignment(Pos.CENTER);

        Label pauseLabel = new Label("Game Paused");
        pauseLabel.setId("pauseLabel"); // Set an ID for the label

        Button resumeButton = new Button("Resume Game");
        resumeButton.setOnAction(e -> togglePauseAction.run());

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> exitAction.run());

        pauseMenuVBox.getChildren().addAll(pauseLabel, resumeButton, exitButton);
        pauseMenuVBox.setVisible(false); // Initially invisible

        return pauseMenuVBox;
    }

    /**
     * Creates and returns a VBox containing the start menu interface.
     * The start menu includes buttons for loading a saved game, starting a new game, and exiting the game.
     *
     * @param load     The button to load a game, configured with the necessary action.
     * @param newGame  The button to start a new game, configured with the necessary action.
     * @param exitGame The button to exit the game, configured with the necessary action.
     * @return A VBox containing the start menu UI elements.
     */
    public static VBox createStartMenu(Button load, Button newGame, Button exitGame) {

        load.setPrefSize(200, 40); // Width and height
        newGame.setPrefSize(200, 40);
        exitGame.setPrefSize(200, 40);

        VBox startMenuVBox = new VBox(80);
        startMenuVBox.setAlignment(Pos.CENTER); // Center the VBox in the scene

        startMenuVBox.getChildren().addAll(load, newGame, exitGame);

        return startMenuVBox;
    }
}
