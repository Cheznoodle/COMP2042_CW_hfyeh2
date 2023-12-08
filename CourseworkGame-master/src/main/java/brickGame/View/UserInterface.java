package brickGame.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class UserInterface {

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
}
