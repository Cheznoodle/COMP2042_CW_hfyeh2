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
