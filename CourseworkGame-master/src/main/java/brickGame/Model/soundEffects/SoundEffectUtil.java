package brickGame.Model.soundEffects;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Utility class for managing sound effects and background music in the game.
 * It provides methods to play various sound effects, manage the mute state,
 * and control background music playback.
 */
public class SoundEffectUtil {

    private static final Logger LOGGER = Logger.getLogger(SoundEffectUtil.class.getName());

    // Boolean flag to manage the mute state of the sound effects.
    private static boolean isMuted = false;

    // MediaPlayer instances for different sound effects and background music.
    private static MediaPlayer hoverSoundPlayer;
    private static MediaPlayer backgroundMediaPlayer;

    /**
     * Toggles the mute state of all sound effects in the game.
     * When muted, all sound effects will be disabled.
     */
    public static void toggleMute() { // New method
        isMuted = !isMuted;
    }

    /**
     * Checks if the sound effects are currently muted.
     *
     * @return true if sound effects are muted, false otherwise.
     */
    public static boolean isMuted() {
        return isMuted;
    }

    /**
     * Plays the sound effect for adding a heart to the player.
     * The sound effect will not play if the sound is muted.
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
     * The sound effect will not play if the sound is muted.
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
     * The sound effect will not play if the sound is muted.
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
     * This static block loads the hover sound resource and prepares the MediaPlayer for playback.
     * It sets up the MediaPlayer to handle the hover sound effect used in the game.
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
     * This method is triggered when the mouse hovers over interactive elements in the game,
     * providing an auditory feedback for the user interaction.
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

    /**
     * Plays background music for the game.
     * This method initiates playback of a specified audio file, looping it indefinitely.
     * The method ensures only one instance of the background music is playing at any time.
     *
     * @param resourcePath The path to the audio file to be played as background music.
     */
    public static void playBackgroundSound(String resourcePath) {
        if (backgroundMediaPlayer == null) {
            try {
                URL resource = SoundEffectUtil.class.getResource(resourcePath);
                if (resource == null) {
                    LOGGER.log(Level.SEVERE, "Audio file not found: " + resourcePath);
                    return;
                }
                Media media = new Media(resource.toExternalForm());
                backgroundMediaPlayer = new MediaPlayer(media);
                backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
                backgroundMediaPlayer.setVolume(0.5);
                backgroundMediaPlayer.play();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in playBackgroundSound method", e);
            }
        }
    }

    /**
     * Pauses the currently playing background music.
     * This method is used to pause the background music when the game is not in the foreground
     * or during certain game events where music should be paused.
     */
    public static void pauseBackgroundMusic() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            backgroundMediaPlayer.pause(); // Pause the media if it's playing.
        }
    }

    /**
     * Resumes playing the background music.
     * This method is used to resume the background music after it has been paused.
     */
    public static void playBackgroundMusic() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            backgroundMediaPlayer.play(); // Resume playback if not already playing.
        }
    }

    /**
     * Mutes the background music.
     * This method is used to mute the background music without stopping its playback,
     * useful for temporarily silencing the music without losing the current playback position.
     */
    public static void muteBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setMute(true); // Mute the media.
        }
    }

    /**
     * Unmutes the background music.
     * This method is used to restore the sound of the background music after it has been muted.
     */
    public static void unmuteBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setMute(false); // Unmute the media.
        }
    }

}
