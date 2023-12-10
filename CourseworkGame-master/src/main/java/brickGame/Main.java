package brickGame;

import brickGame.Model.GameEngine;
import brickGame.View.UserInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

import javafx.scene.layout.VBox;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.layout.StackPane;

import brickGame.soundEffects.SoundEffectUtil;

import brickGame.View.imageEffects.ImageEffectUtil;

import brickGame.Controller.GameController;

import brickGame.View.stageEffects.StageEffectUtil;


/**
 * Main class for the Brick Game.
 * Sets up the game environment, manages game states, handles user interactions,
 * and creates the main game window. This class is the entry point for the application.
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    // Class variable declarations
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private int level = 0;

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private MediaPlayer backgroundMediaPlayer;

    private final int breakWidth     = 130;
    private final int breakHeight    = 30;
    private final int halfBreakWidth = breakWidth / 2;

    private final int sceneWidth = 500;
    private final int sceneHeight = 700;

    private Button load;
    private Button newGame;
    private Button exitGame;

    private StackPane layeredRoot; // New StackPane to manage layers
    private VBox startMenuVBox; // Class-level variable for the VBox

    private VBox pauseMenuVBox; // Pause menu container

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;

    private Rectangle rect;
    private final int       ballRadius = 10;

    private int destroyedBlockCount = 0;

    private final double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;

    private GameEngine engine;
    public static String savePath    = "D:/save/save.mdds";
    public static String savePathDir = "D:/save/";

    private final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> chocos = new ArrayList<>();

    private final Color[]          colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;

    private GameController gameController;

    Stage  primaryStage;


    /**
     * Starts and initializes the game application.
     * Sets up the primary stage, initializes game elements, and handles the main game loop.
     * @param primaryStage The primary stage for this JavaFX application.
     * @throws Exception if an error occurs during initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        load = new Button("Load Game");
        newGame = new Button("Start New Game");
        exitGame = new Button("Exit Game");

        // Attach hover listeners
        attachHoverListener(load);
        attachHoverListener(newGame);
        attachHoverListener(exitGame);

        // Use the method from UserInterface to create the start menu
        startMenuVBox = UserInterface.createStartMenu(load, newGame, exitGame);

        // Create the layered root
        layeredRoot = new StackPane();
        root = new Pane(); // Your existing root pane


        // Add the game root and the start menu to the layered root
        layeredRoot.getChildren().addAll(root, startMenuVBox);

        // Initialize the pause menu
        Runnable togglePauseAction = () -> togglePause(); // Define the Runnable for toggling pause
        pauseMenuVBox = UserInterface.initializePauseMenu(primaryStage, sceneWidth, sceneHeight, togglePauseAction);

        // Add the pause menu to the main layout
        layeredRoot.getChildren().add(pauseMenuVBox);

        // Add the pause menu to the layered root but make it invisible initially
        pauseMenuVBox.setVisible(false);

        //Call the Method in Application's Start Method
        playBackgroundSound();

        if (!loadFromSave) {
            level++;
            if (level >1){
                new Score().showMessage("Level Up :)", this);
            }
            //Win Game once reach Lvl 18
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            initBall();
            initBreak();
            initBoard();


        }

        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);

        scoreLabel.setId("scoreLabel");
        heartLabel.setId("heartLabel");
        levelLabel.setId("levelLabel");

        root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, load, newGame, exitGame);

        for (Block block : blocks) {
            root.getChildren().add(block.getRect());
        }

        this.gameController = new GameController(this);
        Scene scene = new Scene(root, sceneWidth, sceneHeight);

        // Load the CSS file with error handling
        URL resource = getClass().getResource("/style.css");
        if (resource == null) {
            System.err.println("Unable to load style.css");

        } else {
            scene.getStylesheets().add(resource.toExternalForm());
        }

        scene.setOnKeyPressed(this);

        // Instantiate GameController and set it as the event handler
        GameController gameController = new GameController(this);
        scene.setOnKeyPressed(gameController);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Centering buttons after the scene is rendered
        Platform.runLater(() -> {
            double loadButtonWidth = load.getWidth();
            double newGameButtonWidth = newGame.getWidth();
            double exitButtonWidth = exitGame.getWidth();

            load.setLayoutX((sceneWidth - loadButtonWidth) / 2);
            load.setLayoutY(sceneHeight / 2.0 - 50); // Vertical positioning

            newGame.setLayoutX((sceneWidth - newGameButtonWidth) / 2);
            newGame.setLayoutY(sceneHeight / 2.0 + 10); // Vertical positioning

            exitGame.setLayoutX((sceneWidth - exitButtonWidth) / 2);
            exitGame.setLayoutY(sceneHeight / 2.0 + 70);
        });

        if (!loadFromSave) {
            if (level > 1 && level < 18) {
                load.setVisible(false);
                newGame.setVisible(false);
                exitGame.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            load.setOnAction(event -> {
                loadGame();
                load.setVisible(false);
                newGame.setVisible(false);
                exitGame.setVisible(false);
            });

            newGame.setOnAction(event -> {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
                load.setVisible(false);
                newGame.setVisible(false);
                exitGame.setVisible(false);
            });

            exitGame.setOnAction(event -> Platform.exit());

        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }

    }


    /**
     * Attaches a hover sound effect listener to a button.
     * @param button The button to attach the listener to.
     */
    private void attachHoverListener(Button button) {
        button.setOnMouseEntered(e -> SoundEffectUtil.playHoverSound());
    }

    /**
     * Toggles the sound effects and background music on or off.
     */
    public void toggleSound() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setMute(!backgroundMediaPlayer.isMute());
        }
        SoundEffectUtil.toggleMute(); // Toggle mute for sound effects
    }


    /**
     * Initializes the game board by creating and placing blocks.
     * The number and types of blocks depend on the current game level.
     */
    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);


                int type;
                if (r % 10 == 1) {
                    type = Block.getBlockChoco();
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.getBlockHeart();
                        isExistHeartBlock = true;
                    } else {
                        type = Block.getBlockNormal();
                    }
                } else if (r % 10 == 3) {
                    type = Block.getBlockStar();
                } else {
                    type = Block.getBlockNormal();
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }

    /**
     * Handles key events for the game, delegating to the GameController.
     * @param event The KeyEvent to handle.
     */
    @Override
    public void handle(KeyEvent event) {
        gameController.handle(event);
    }

    /**
     * Updates the position of the player's paddle in response to user input.
     * @param delta The amount to move the paddle.
     */
    public void updatePaddlePosition(int delta) {
        // Update xBreak within the bounds of the scene
        if ((delta > 0 && xBreak + breakWidth < sceneWidth) || (delta < 0 && xBreak > 0)) {
            xBreak += delta;
            centerBreakX = xBreak + halfBreakWidth;
        }
    }

    /**
     * Toggles the game's pause state, pausing or resuming the game.
     */
    public void togglePause() {
        if (engine.isPaused()) {
            engine.resume();
            pauseMenuVBox.setVisible(false);
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.play(); // Resume the background sound
            }
        } else {
            engine.pause();
            pauseMenuVBox.setVisible(true);
            if (backgroundMediaPlayer != null) {
                backgroundMediaPlayer.pause(); // Pause the background sound
            }
        }
    }

    /**
     * Plays background sound for the game.
     * The sound loops indefinitely until stopped.
     */
    private void playBackgroundSound() {
        if (backgroundMediaPlayer == null) {
            try {
                URL resource = getClass().getResource("/backgroundSound/wii.mp3");
                if (resource == null) {
                    System.err.println("Audio file not found!");
                    return;
                }
                Media media = new Media(resource.toString());
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
     * Main method to launch the JavaFX application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Initializes the ball for the game.
     * Sets the initial position and graphical representation of the ball.
     */
    private void initBall() {
        // Set fixed coordinates for the ball's initial position
        xBall = sceneWidth / 2.0; // Spawn in the middle of the scene width-wise
        yBall = sceneHeight / 2.0; // Spawn in the middle of the scene height-wise

        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(loadImagePattern("ball.png"));
    }


    /**
     * Initializes the player's paddle (breaker) with its starting position and appearance.
     */
    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);
        rect.setFill(loadImagePattern("block.png"));
    }

    /**
     * Loads an image pattern from a given file path.
     * Used to create graphical representations for game objects.
     *
     * @param imagePath The path to the image file.
     * @return The loaded ImagePattern.
     */
    private ImagePattern loadImagePattern(String imagePath) {
        return new ImagePattern(new Image(imagePath));
    }

    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean collideToBreak = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;
    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;

    private double vX = 1.000;
    private double vY = 1.000;

    private void resetCollideFlags() {

        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    private void setPhysicsToBall() {

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall <= 0) {

            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeight) {
            goDownBall = false;
            if (!isGoldStatus) {

                heart--;
                SoundEffectUtil.playMinusHeartSoundEffect();
                StageEffectUtil.shakeStage(primaryStage);
                ImageEffectUtil.showHeartDeductedImage(root, sceneWidth, sceneHeight);


                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }

        }

        if (yBall >= yBreak - ballRadius) {

            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2.0);

                if (Math.abs(relation) <= 0.3) {

                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);

                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);

                }

                collideToBreakAndMoveToRight = xBall - centerBreakX > 0;

            }
        }

        if (xBall >= sceneWidth) {
            resetCollideFlags();

            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetCollideFlags();

            collideToLeftWall = true;
        }

        if (collideToBreak) {
            goRightBall = collideToBreakAndMoveToRight;
        }

        //Wall Collide

        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Collide

        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = true;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }


    }



    /**
     * Checks if all blocks have been destroyed and proceeds to the next level if so.
     */
    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {

            nextLevel();
        }
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        new Thread(() -> {
            // Define the relative path for the directory
            String relativeDir = "saves";
            File directory = new File(relativeDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            // Define the file name
            String saveFileName = "save.mdds";
            File file = new File(directory, saveFileName);

            // Save game data to the file
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                // Write game data to the file
                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);
                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);
                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToBreak);
                outputStream.writeBoolean(collideToBreakAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                // Serialize and write the blocks array
                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : blocks) {
                    if (!block.isDestroyed()) {
                        String colorString = block.getColor().toString(); // Convert color to string
                        blockSerializable.add(new BlockSerializable(block.getRow(), block.getColumn(), block.getType(), colorString));
                    }
                }

                outputStream.writeObject(blockSerializable);

                LOGGER.info("Game saved successfully to " + file.getPath());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error saving game", e);
            }
        }).start();
    }

    /**
     * Loads a saved game state from a file.
     */
    private void loadGame() {
        // Define the relative path for the directory and file
        String relativeDir = "saves";
        String saveFileName = "save.mdds";
        File file = new File(relativeDir, saveFileName);

        // Load game data from the file
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                // Read game data from the file
                level = inputStream.readInt();
                score = inputStream.readInt();
                heart = inputStream.readInt();
                destroyedBlockCount = inputStream.readInt();
                xBall = inputStream.readDouble();
                yBall = inputStream.readDouble();
                xBreak = inputStream.readDouble();
                yBreak = inputStream.readDouble();
                centerBreakX = inputStream.readDouble();
                time = inputStream.readLong();
                goldTime = inputStream.readLong();
                vX = inputStream.readDouble();
                isExistHeartBlock = inputStream.readBoolean();
                isGoldStatus = inputStream.readBoolean();
                goDownBall = inputStream.readBoolean();
                goRightBall = inputStream.readBoolean();
                collideToBreak = inputStream.readBoolean();
                collideToBreakAndMoveToRight = inputStream.readBoolean();
                collideToRightWall = inputStream.readBoolean();
                collideToLeftWall = inputStream.readBoolean();
                collideToRightBlock = inputStream.readBoolean();
                collideToBottomBlock = inputStream.readBoolean();
                collideToLeftBlock = inputStream.readBoolean();
                collideToTopBlock = inputStream.readBoolean();

                // Deserialize and read the blocks array
                ArrayList<BlockSerializable> blockSerializable = (ArrayList<BlockSerializable>) inputStream.readObject();
                blocks.clear();
                chocos.clear();
                for (BlockSerializable ser : blockSerializable) {

                    Color color = Color.valueOf(ser.colorString); // Convert string back to color
                    blocks.add(new Block(ser.row, ser.j, color, ser.type));
                }

                LOGGER.info("Game loaded successfully from " + file.getPath());
                loadFromSave = true;

                try {
                    start(primaryStage);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error starting the game from loadGame", e);
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Error loading game", e);
            }
        } else {
            LOGGER.log(Level.WARNING, "Save file does not exist: " + file.getPath());
        }
    }

    /**
     * Advances the game to the next level, resetting necessary variables and states.
     */
    private void nextLevel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    vX = 1.000;

                    engine.stop();
                    resetCollideFlags();
                    goDownBall = true;

                    isGoldStatus = false;
                    isExistHeartBlock = false;


                    hitTime = 0;
                    time = 0;
                    goldTime = 0;

                    engine.stop();
                    blocks.clear();
                    chocos.clear();
                    destroyedBlockCount = 0;
                    start(primaryStage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Restarts the entire game, resetting all game variables and states.
     */
    public void restartGame() {

        try {
            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the game state, typically called in each frame.
     */
    @Override
    public void onUpdate() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {


                scoreLabel.setText("Score: " + score);
                heartLabel.setText("Heart : " + heart);

                rect.setX(xBreak);
                rect.setY(yBreak);
                ball.setCenterX(xBall);
                ball.setCenterY(yBall);

                for (Bonus choco : chocos) {
                    choco.choco.setY(choco.y);
                }
            }
        });


        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                if (hitCode != Block.getNoHit()) { // Updated to use the getter method
                    score += 1;

                    // Use getters to access the properties of Block
                    new Score().show(block.getX(), block.getY(), 1, this);

                    // Use the setter methods to modify Block instances
                    block.getRect().setVisible(false);
                    block.setDestroyed(true);

                    destroyedBlockCount++;
                    resetCollideFlags();

                    // Use getters for block type checks
                    if (block.getType() == Block.getBlockChoco()) {
                        final Bonus choco = new Bonus(block.getRow(), block.getColumn());
                        choco.timeCreated = time;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                root.getChildren().add(choco.choco);
                            }
                        });
                        chocos.add(choco);
                    }

                    if (block.getType() == Block.getBlockStar()) {
                        System.out.println("You are Invincible for 12 seconds!");
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("goldball.png")));
                        root.getStyleClass().add("goldRoot");
                        isGoldStatus = true;

                        ImageEffectUtil.showGoldenBallImage(root, sceneWidth, sceneHeight); // Call to display the golden ball image
                    }

                    if (block.getType() == Block.getBlockHeart()) {
                        heart++;
                        ImageEffectUtil.showHeartAddedImage(root, sceneWidth, sceneHeight);
                    }

                    // Update the collision logic based on hitCode using getters
                    if (hitCode == Block.getHitRight()) {
                        collideToRightBlock = true;
                    } else if (hitCode == Block.getHitBottom()) {
                        collideToBottomBlock = true;
                    } else if (hitCode == Block.getHitLeft()) {
                        collideToLeftBlock = true;
                    } else if (hitCode == Block.getHitTop()) {
                        collideToTopBlock = true;
                    }
                }
            }
        }


    }

    /**
     * Handles rendering of the game, if necessary.
     */
    @Override
    public void onRender() {

    }

    /**
     * Performs initial setup for the game.
     */
    @Override
    public void onInit() {

    }

    /**
     * Stops the game, typically called when the application is closing.
     */
    @Override
    public void stop() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }
    }

    /**
     * Updates the physics aspects of the game, such as ball movement and collision detection.
     */
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();


        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > sceneHeight || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;

                ImageEffectUtil.showBonusImage(root, sceneWidth, sceneHeight);
                SoundEffectUtil.playBonusSoundEffect();

                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

    }

    /**
     * Updates the in-game timer.
     * @param time The current time value.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
