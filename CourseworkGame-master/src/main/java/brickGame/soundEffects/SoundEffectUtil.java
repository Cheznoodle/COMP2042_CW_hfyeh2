package brickGame.soundEffects;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Utility class for handling sound effects in the game.
 * It provides methods to play different sound effects and manage the mute state.
 */
public class SoundEffectUtil {

    private static final Logger LOGGER = Logger.getLogger(SoundEffectUtil.class.getName());

    private static boolean isMuted = false; // Flag to manage the mute state of the sound effects

    // MediaPlayer for hover sound effect
    private static MediaPlayer hoverSoundPlayer;

    /**
     * Toggles the mute state of sound effects.
     * If muted, all sound effects will be disabled.
     */
    public static void toggleMute() { // New method
        isMuted = !isMuted;
    }

    /**
     * Plays the sound effect for adding a heart.
     * This method checks if the sound is muted before playing the effect.
     */
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

    /**
     * Plays the sound effect for losing a heart.
     * This method checks if the sound is muted before playing the effect.
     */
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

    /**
     * Plays the sound effect for collecting a bonus item.
     * This method checks if the sound is muted before playing the effect.
     */
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

    /**
     * Initializes the MediaPlayer for the hover sound effect.
     * This static block loads the hover sound resource and sets up the MediaPlayer.
     */
    static {
        try {
            URL resource = SoundEffectUtil.class.getResource("/soundFX/hoverSound.mp3");
            if (resource == null) {
                LOGGER.log(Level.SEVERE, "Sound file not found: /hoverSound.mp3");
            } else {
                hoverSoundPlayer = new MediaPlayer(new Media(resource.toExternalForm()));
                hoverSoundPlayer.setOnError(() -> LOGGER.log(Level.SEVERE, "Error in MediaPlayer: " + hoverSoundPlayer.getError().getMessage()));
                hoverSoundPlayer.setVolume(1.0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception in static initializer for hoverSoundPlayer", e);
        }
    }

    /**
     * Plays the hover button sound effect.
     * This method is triggered when the mouse hovers over interactive elements in the game.
     */
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
