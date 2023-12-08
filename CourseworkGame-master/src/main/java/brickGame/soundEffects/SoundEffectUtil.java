package brickGame.soundEffects;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffectUtil {

    private static final Logger LOGGER = Logger.getLogger(SoundEffectUtil.class.getName());

    private static boolean isMuted = false;

    // Declare MediaPlayer for hover sound as a class member
    private static MediaPlayer hoverSoundPlayer;

    public static void toggleMute() { // New method
        isMuted = !isMuted;
    }

    public static void playAddHeartSoundEffect() {
        if (isMuted) return; // Check mute state

        try {
            URL resource = SoundEffectUtil.class.getResource("/soundFX/ting.mp3");
            if (resource == null) {
                LOGGER.log(Level.SEVERE, "Sound file not found: /ting.mp3");
                return;
            }
            Media sound = new Media(resource.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);

            mediaPlayer.setOnError(() -> LOGGER.log(Level.SEVERE, "Error in MediaPlayer: " + mediaPlayer.getError().getMessage()));

            mediaPlayer.setOnReady(() -> mediaPlayer.play());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception in playAddHeartSoundEffect method", e);
        }
    }

    public static void playMinusHeartSoundEffect() {
        if (isMuted) return; // Check mute state

        Platform.runLater(() -> {
            try {
                URL resource = SoundEffectUtil.class.getResource("/soundFX/oof.mp3");
                if (resource == null) {
                    LOGGER.log(Level.SEVERE, "Sound file not found: /oof.mp3");
                    return;
                }
                Media sound = new Media(resource.toExternalForm());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);

                mediaPlayer.setOnError(() -> LOGGER.log(Level.SEVERE, "Error in MediaPlayer: " + mediaPlayer.getError().getMessage()));

                mediaPlayer.setOnReady(() -> {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                        mediaPlayer.play();
                    }
                });

                mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.dispose()); // Dispose the player after playing

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in playMinusHeartSoundEffect method", e);
            }
        });
    }

    public static void playBonusSoundEffect() {
        if (isMuted) return; // Check mute state

        Platform.runLater(() -> {
            try {
                URL resource = SoundEffectUtil.class.getResource("/soundFX/minikit.mp3");
                if (resource == null) {
                    LOGGER.log(Level.SEVERE, "Sound file not found: /minikit.mp3");
                    return;
                }
                Media sound = new Media(resource.toExternalForm());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);

                mediaPlayer.setOnError(() -> LOGGER.log(Level.SEVERE, "Error in MediaPlayer: " + mediaPlayer.getError().getMessage()));

                mediaPlayer.setOnReady(() -> {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                        mediaPlayer.play();
                    }
                });

                mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.dispose()); // Dispose the player after playing

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in playBonusSoundEffect method", e);
            }
        });
    }

    // Static block to initialize hover sound MediaPlayer
    static {
        try {
            URL resource = SoundEffectUtil.class.getResource("/soundFX/hoverSound.mp3");
            if (resource == null) {
                LOGGER.log(Level.SEVERE, "Sound file not found: /hoverSound.mp3");
            } else {
                hoverSoundPlayer = new MediaPlayer(new Media(resource.toExternalForm()));
                hoverSoundPlayer.setOnError(() -> LOGGER.log(Level.SEVERE, "Error in MediaPlayer: " + hoverSoundPlayer.getError().getMessage()));
                hoverSoundPlayer.setVolume(1.0); // Set volume (range 0.0 to 1.0)
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception in static initializer for hoverSoundPlayer", e);
        }
    }

    // Method to play hover button sound effect
    public static void playHoverSound() {
        if (hoverSoundPlayer == null) {
            LOGGER.log(Level.SEVERE, "Hover sound player not initialized");
            return;
        }

        if (isMuted) return; // Check mute state

        Platform.runLater(() -> {
            hoverSoundPlayer.stop(); // Stop any currently playing sound
            hoverSoundPlayer.play(); // Play the sound
        });
    }

}
