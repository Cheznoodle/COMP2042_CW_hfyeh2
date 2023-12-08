package brickGame.View.stageEffects;

import javafx.application.Platform;
import javafx.stage.Stage;

public class StageEffectUtil {

    /**
     * Shakes the given stage to create a visual effect.
     * This effect is typically used to indicate game events like losing a heart.
     * @param stage The stage to be shaken.
     */
    public static void shakeStage(Stage stage) {
        final double originalX = stage.getX();
        final double originalY = stage.getY();
        final int shakeDistance = 5;
        final int shakeCycles = 10;

        for (int i = 0; i < shakeCycles; i++) {
            Platform.runLater(() -> {
                stage.setX(originalX + Math.random() * shakeDistance - shakeDistance / 2.0);
                stage.setY(originalY + Math.random() * shakeDistance - shakeDistance / 2.0);
            });

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                stage.setX(originalX);
                stage.setY(originalY);
            });
        }
    }
}
