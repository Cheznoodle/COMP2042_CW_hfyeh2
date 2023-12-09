package brickGame.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * Provides utility functions for creating user interface elements in the game.
 */
public class UserInterface {

    /**
     * Initializes and returns a VBox containing the pause menu interface.
     * This menu includes a label and a resume button.
     *
     * @param primaryStage     The primary stage of the application.
     * @param sceneWidth       The width of the scene, used for positioning.
     * @param sceneHeight      The height of the scene, used for positioning.
     * @param togglePauseAction A Runnable action that defines what happens when the resume button is clicked.
     * @return VBox containing the pause menu UI elements.
     */
    public static VBox initializePauseMenu(Stage primaryStage, double sceneWidth, double sceneHeight, Runnable togglePauseAction) {
        VBox pauseMenuVBox = new VBox(10);
        pauseMenuVBox.setAlignment(Pos.CENTER);
        pauseMenuVBox.setTranslateX((sceneWidth - pauseMenuVBox.getWidth()) / 2.0);
        pauseMenuVBox.setTranslateY((sceneHeight - pauseMenuVBox.getHeight()) / 2.0);

        Label pauseLabel = new Label("Game Paused");
        Button resumeButton = new Button("Resume Game");
        resumeButton.setOnAction(e -> togglePauseAction.run());
        pauseMenuVBox.getChildren().addAll(pauseLabel, resumeButton);
        pauseMenuVBox.setVisible(false); // Initially invisible

        return pauseMenuVBox;
    }

    /**
     * Creates a VBox containing the start menu interface.
     * This menu includes buttons for loading a game, starting a new game, and exiting the game.
     *
     * @param load     Button to load a game.
     * @param newGame  Button to start a new game.
     * @param exitGame Button to exit the game.
     * @return VBox containing the start menu UI elements.
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
