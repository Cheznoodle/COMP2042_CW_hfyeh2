package brickGame.imageEffects;

import brickGame.soundEffects.SoundEffectUtil;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class ImageEffectUtil {

    /**
     * Displays an image indicating a heart deduction.
     * This method is invoked when the player loses a heart.
     */
    public static void showHeartDeductedImage(Pane root, int sceneWidth, int sceneHeight) {
        Platform.runLater(() -> {
            // Create an ImageView and attempt to load the image
            Image image = new Image("imagefx/minusHeart.png", 200, 200, true, true);
            ImageView heartImage = new ImageView(image);

            // Check if the image has been loaded correctly
            if (image.isError()) {
                System.out.println("Error loading minus heart image");
                return; // Exit if the image hasn't been loaded correctly
            }

            VBox imageContainer = new VBox(heartImage);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefSize(sceneWidth, sceneHeight);

            // Initially set the image to be transparent
            imageContainer.setOpacity(0);

            root.getChildren().add(imageContainer);

            // Fade in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.setDelay(Duration.seconds(0.5)); // Delay to keep the image visible

            // Start fade in transition
            fadeIn.play();

            // After fade in, start fade out
            fadeIn.setOnFinished(event -> fadeOut.play());

            // After fade out, remove the image container
            fadeOut.setOnFinished(event -> root.getChildren().remove(imageContainer));
        });


    }

    public static void showHeartAddedImage(Pane root, int sceneWidth, int sceneHeight) {
        // Play heart added sound effect
        SoundEffectUtil.playAddHeartSoundEffect();

        Platform.runLater(() -> {
            // Create an ImageView and attempt to load the image
            Image image = new Image("imagefx/plusHeart.png", 400, 400, true, true);
            ImageView heartImage = new ImageView(image);

            // Check if the image has been loaded correctly
            if (image.isError()) {
                System.out.println("Error loading plus heart image");
                return; // Exit if the image hasn't been loaded correctly
            }

            VBox imageContainer = new VBox(heartImage);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefSize(sceneWidth, sceneHeight);

            // Initially set the image to be transparent
            imageContainer.setOpacity(0);

            root.getChildren().add(imageContainer);

            // Fade in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.setDelay(Duration.seconds(0.5)); // Delay to keep the image visible

            // Start fade in transition
            fadeIn.play();

            // After fade in, start fade out
            fadeIn.setOnFinished(event -> fadeOut.play());

            // After fade out, remove the image container
            fadeOut.setOnFinished(event -> root.getChildren().remove(imageContainer));
        });


    }


    public static void showBonusImage(Pane root, int sceneWidth, int sceneHeight) {
        Platform.runLater(() -> {
            // Create an ImageView and attempt to load the image
            Image image = new Image("imagefx/bonusPicture.png", 250, 250, true, true);
            ImageView heartImage = new ImageView(image);

            // Check if the image has been loaded correctly
            if (image.isError()) {
                System.out.println("Error loading bonus image");
                return; // Exit if the image hasn't been loaded correctly
            }

            VBox imageContainer = new VBox(heartImage);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefSize(sceneWidth, sceneHeight);

            // Initially set the image to be transparent
            imageContainer.setOpacity(0);

            root.getChildren().add(imageContainer);

            // Fade in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.setDelay(Duration.seconds(0.5)); // Delay to keep the image visible

            // Start fade in transition
            fadeIn.play();

            // After fade in, start fade out
            fadeIn.setOnFinished(event -> fadeOut.play());

            // After fade out, remove the image container
            fadeOut.setOnFinished(event -> root.getChildren().remove(imageContainer));
        });


    }

    public static void showGoldenBallImage(Pane root, int sceneWidth, int sceneHeight) {
        Platform.runLater(() -> {
            // Create an ImageView and attempt to load the image
            Image image = new Image("imagefx/goldenBonus.png", 350, 350, true, true);
            ImageView heartImage = new ImageView(image);

            // Check if the image has been loaded correctly
            if (image.isError()) {
                System.out.println("Error loading golden ball image");
                return; // Exit if the image hasn't been loaded correctly
            }

            VBox imageContainer = new VBox(heartImage);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefSize(sceneWidth, sceneHeight);

            // Initially set the image to be transparent
            imageContainer.setOpacity(0);

            root.getChildren().add(imageContainer);

            // Fade in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), imageContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.setDelay(Duration.seconds(0.5)); // Delay to keep the image visible

            // Start fade in transition
            fadeIn.play();

            // After fade in, start fade out
            fadeIn.setOnFinished(event -> fadeOut.play());

            // After fade out, remove the image container
            fadeOut.setOnFinished(event -> root.getChildren().remove(imageContainer));
        });


    }




}
