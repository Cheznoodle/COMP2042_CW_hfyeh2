package brickGame.View;

import brickGame.Model.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Manages the display of scores, messages, and game status notifications within the game.
 * This class is responsible for animating and showing score updates, game-over messages,
 * win messages, and other interactive elements on the game screen.
 */
public class Score {

    // Constants for label positioning and animation
    private static final double MESSAGE_X = 220;
    private static final double MESSAGE_Y = 340;
    private static final int ANIMATION_DURATION = 15; // in milliseconds
    private static final int ANIMATION_COUNT = 20;
    private static final double SCALE_INCREMENT = 0.1;

    /**
     * Displays a score indicator at a specified location on the game screen.
     * Animates the score label for visual emphasis.
     *
     * @param x     The x-coordinate where the score should be displayed.
     * @param y     The y-coordinate where the score should be displayed.
     * @param score The numerical value of the score to be displayed.
     * @param main  Reference to the main game class, used for adding the label to the game's root pane.
     */
    public void show(final double x, final double y, int score, final Main main) {
        String sign = (score >= 0) ? "+" : "";
        Label label = createLabel(sign + score, x, y);
        animateAndAddLabel(label, main);
    }

    /**
     * Displays a text message on the game screen.
     * The message is displayed at a predefined central position with animation.
     *
     * @param message The text message to be displayed.
     * @param main    Reference to the main game class, used for adding the label to the game's root pane.
     */
    public void showMessage(String message, final Main main) {
        Label label = createLabel(message, MESSAGE_X, MESSAGE_Y);
        animateAndAddLabel(label, main);
    }

    /**
     * Creates and returns a new label with specified text and position.
     * This label is used for displaying scores and messages.
     *
     * @param text The text to be displayed in the label.
     * @param x    The x-coordinate for positioning the label.
     * @param y    The y-coordinate for positioning the label.
     * @return The newly created label.
     */
    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    /**
     * Animates a label with scaling and fading effects, then adds it to the game's root pane.
     * This method is used to visually emphasize score updates and messages.
     *
     * @param label The label to animate and display.
     * @param main  Reference to the main game class, used for adding the animated label to the game's root pane.
     */
    private void animateAndAddLabel(final Label label, final Main main) {
        Platform.runLater(() -> {
            main.root.getChildren().add(label);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), e -> {
                double scale = label.getScaleX() + SCALE_INCREMENT;
                label.setScaleX(scale);
                label.setScaleY(scale);
                label.setOpacity(1 - scale / 2.1);
            }));
            timeline.setCycleCount(ANIMATION_COUNT);
            timeline.play();
        });
    }

    /**
     * Displays the 'Game Over' message on the screen along with options to restart or exit the game.
     * This method is called when the player loses all lives or fails to complete the game.
     *
     * @param main Reference to the main game class, used for adding elements to the game's root pane.
     */
    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("  Game Over");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> main.restartGame());

            Button exit = new Button("Exit");
            exit.setTranslateX(220);
            exit.setTranslateY(350 + 20);
            exit.setOnAction(event -> Platform.exit()); // Action to exit the application

            main.root.getChildren().addAll(label, restart, exit);

            main.playGameOverVideo();
        });
    }

    /**
     * Displays the 'You Win' message on the screen.
     * This method is called when the player successfully completes the game.
     *
     * @param main Reference to the main game class, used for adding the winning message to the game's root pane.
     */
    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            main.root.getChildren().addAll(label);
        });
    }
}
