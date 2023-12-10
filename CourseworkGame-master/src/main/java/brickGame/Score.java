package brickGame;

import brickGame.Model.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Manages the display of scores and messages within the game.
 * This class is responsible for showing score updates, game-over messages, and win messages
 * with animation effects on the game screen.
 */
public class Score {

    // Constants for label positioning and animation
    private static final double MESSAGE_X = 220;
    private static final double MESSAGE_Y = 340;
    private static final int ANIMATION_DURATION = 15; // in milliseconds
    private static final int ANIMATION_COUNT = 20;
    private static final double SCALE_INCREMENT = 0.1;

    /**
     * Displays a score indicator at a specified location in the game.
     *
     * @param x     The x-coordinate for the score display.
     * @param y     The y-coordinate for the score display.
     * @param score The score to display.
     * @param main  Reference to the main game class for accessing the game's root pane.
     */
    public void show(final double x, final double y, int score, final Main main) {
        String sign = (score >= 0) ? "+" : "";
        Label label = createLabel(sign + score, x, y);
        animateAndAddLabel(label, main);
    }

    /**
     * Displays a message on the screen at a predefined position.
     *
     * @param message The message to be displayed.
     * @param main    Reference to the main game class for accessing the game's root pane.
     */
    public void showMessage(String message, final Main main) {
        Label label = createLabel(message, MESSAGE_X, MESSAGE_Y);
        animateAndAddLabel(label, main);
    }

    /**
     * Creates a label with specified text and position.
     *
     * @param text The text for the label.
     * @param x    The x-coordinate for the label.
     * @param y    The y-coordinate for the label.
     * @return The created label.
     */
    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    /**
     * Animates a label by increasing its scale and fading it out, then adds it to the game's root pane.
     *
     * @param label The label to animate.
     * @param main  Reference to the main game class for adding the label to the game's root pane.
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
     * Displays the 'Game Over' message with a restart button on the screen.
     *
     * @param main Reference to the main game class for adding elements to the game's root pane.
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

            main.root.getChildren().addAll(label, restart);
        });
    }

    /**
     * Displays the 'You Win' message on the screen.
     *
     * @param main Reference to the main game class for adding elements to the game's root pane.
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
