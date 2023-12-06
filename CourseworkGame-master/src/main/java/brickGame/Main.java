package brickGame;

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
import javafx.geometry.Pos;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.layout.StackPane;

import brickGame.soundEffects.SoundEffectUtil;

import brickGame.imageEffects.ImageEffectUtil;


/**
 * Main class for the Brick Game.
 * This class sets up the game environment, manages game states, and handles user interactions.
 * It creates the main game window, initializes game components, and starts the game loop.
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

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

    private static final int LEFT  = 1;
    private static final int RIGHT = 2;

    private Button load = new Button("Load Game");
    private Button newGame = new Button("Start New Game");

    private Button exitGame = new Button("Exit Game");

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

    Stage  primaryStage;

    /**
     * Initializes and starts the game.
     * This method sets up the primary stage, initializes game elements,
     * and handles the main game loop and events.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Create the layered root
        layeredRoot = new StackPane();
        root = new Pane(); // Your existing root pane
        createStartMenu(); // Call the method to create the start menu

        // Add the game root and the start menu to the layered root
        layeredRoot.getChildren().addAll(root, startMenuVBox);

        // Initialize the pause menu
        initializePauseMenu();

        // Add the pause menu to the layered root but make it invisible initially
        //layeredRoot.getChildren().add(pauseMenuVBox);
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

            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            exitGame = new Button("Exit Game");

            // Call the method to create the start menu
            createStartMenu();

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

        Scene scene = new Scene(root, sceneWidth, sceneHeight);

        // Load the CSS file with error handling
        URL resource = getClass().getResource("/style.css");
        if (resource == null) {
            System.err.println("Unable to load style.css");

        } else {
            scene.getStylesheets().add(resource.toExternalForm());
        }

        scene.setOnKeyPressed(this);

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
     * Shakes the game stage to create a visual effect.
     * This effect is typically used to indicate game events like losing a heart.
     */
    private void shakeStage() {
        final double originalX = primaryStage.getX();
        final double originalY = primaryStage.getY();
        final int shakeDistance = 5;
        final int shakeCycles = 10;

        for (int i = 0; i < shakeCycles; i++) {
            Platform.runLater(() -> {
                primaryStage.setX(originalX + Math.random() * shakeDistance - shakeDistance / 2.0);
                primaryStage.setY(originalY + Math.random() * shakeDistance - shakeDistance / 2.0);
            });

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                primaryStage.setX(originalX);
                primaryStage.setY(originalY);
            });
        }
    }



    private void createStartMenu() {
        startMenuVBox = new VBox(80);
        load = new Button("Load Game");
        newGame = new Button("Start New Game");
        exitGame = new Button("Exit Game");

        startMenuVBox.getChildren().addAll(load, newGame, exitGame);
        startMenuVBox.setAlignment(Pos.CENTER); // Center the VBox in the scene

        // startMenuVBox.setPrefWidth(200); // Example width
        // startMenuVBox.setPrefHeight(150); // Example height

        // Add the VBox to the root pane
        root.getChildren().add(startMenuVBox);
    }


    private void initializePauseMenu() {
        pauseMenuVBox = new VBox(10);
        pauseMenuVBox.setAlignment(Pos.CENTER);
        // Center the VBox in the middle of the scene
        pauseMenuVBox.setTranslateX((sceneWidth - pauseMenuVBox.getWidth()) / 2.0);
        pauseMenuVBox.setTranslateY((sceneHeight - pauseMenuVBox.getHeight()) / 2.0);

        Label pauseLabel = new Label("Game Paused");
        Button resumeButton = new Button("Resume Game");
        resumeButton.setOnAction(e -> togglePause());
        pauseMenuVBox.getChildren().addAll(pauseLabel, resumeButton);
        pauseMenuVBox.setVisible(false); // Initially invisible

        // Add the pause menu to the main layout
        layeredRoot.getChildren().add(pauseMenuVBox);
    }

    private void toggleSound() {
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
                    type = Block.getBlockChoco(); // Updated
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.getBlockHeart(); // Updated
                        isExistHeartBlock = true;
                    } else {
                        type = Block.getBlockNormal(); // Updated
                    }
                } else if (r % 10 == 3) {
                    type = Block.getBlockStar(); // Updated
                } else {
                    type = Block.getBlockNormal(); // Updated
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }


    /**
     * Handles keyboard events for controlling the game.
     * Supports movement, game actions, and other controls based on key presses.
     *
     * @param event The keyboard event that occurred.
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
            case M:
                toggleSound();
                break;
            case ESCAPE:
                togglePause();
                break;
        }
    }


    /**
     * Moves the player's paddle in a specified direction.
     * Creates a new thread for the movement to provide smooth control.
     *
     * @param direction The direction to move the paddle (LEFT or RIGHT).
     */
    private void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 4;
            for (int i = 0; i < 30; i++) {
                // Check for right boundary
                if (direction == RIGHT && xBreak + breakWidth < sceneWidth) {
                    xBreak++;
                }
                // Check for left boundary
                else if (direction == LEFT && xBreak > 0) {
                    xBreak--;
                }

                centerBreakX = xBreak + halfBreakWidth;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Interrupted exception in move method", e);
                    Thread.currentThread().interrupt();
                }
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }

    private void togglePause() {
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
        if (backgroundMediaPlayer == null) { // Only initialize if not already done
            try {
                URL resource = getClass().getResource("/backgroundSound/wii.mp3");
                if (resource == null) {
                    System.err.println("Audio file not found!");
                    return;
                }
                Media media = new Media(resource.toString());
                backgroundMediaPlayer = new MediaPlayer(media);
                backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
                backgroundMediaPlayer.play();
            } catch (Exception e) {
                // Replace printStackTrace with a logging statement
                LOGGER.log(Level.SEVERE, "Exception in playBackgroundSound method", e);
            }
        }
    }



    /**
     * The main entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments (not used).
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
     * Initializes the player's paddle (breaker).
     * Sets the initial position and graphical representation of the paddle.
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


    //ADD ENUMERATION OR A STATE OBJECT FOR THE BALL'S VARIOUS STATES
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
    //REFACTOR ABOVE

    //REFACTOR DONE
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
    //REFACTOR DONE

    //REFACTOR BELOW
    private void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

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
            //vX = 1.000;
            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeight) {
            goDownBall = false;
            if (!isGoldStatus) {
                //TODO gameover
                heart--;
                SoundEffectUtil.playMinusHeartSoundEffect();
                shakeStage();
                ImageEffectUtil.showHeartDeductedImage(root, sceneWidth, sceneHeight);
//                new Score().show(sceneWidth / 2.0, sceneHeight / 2.0, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
            //return;
        }

        if (yBall >= yBreak - ballRadius) {
            //System.out.println("Collide1");
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2.0);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                    //System.out.println("vX " + vX);
                }

                collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
                //System.out.println("Collide2");
            }
        }

        if (xBall >= sceneWidth) {
            resetCollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetCollideFlags();
            //vX = 1.000;
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
    //REFACTOR ABOVE


    //WHAT TO DO IN LAST LEVEL?
    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    private void saveGame() {
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
                        blockSerializable.add(new BlockSerializable(block.getRow(), block.getColumn(), block.getType()));
                    }
                }
                outputStream.writeObject(blockSerializable);

                LOGGER.info("Game saved successfully to " + file.getPath());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error saving game", e);
            }
        }).start();
    }


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
                    int r = new Random().nextInt(colors.length);
                    blocks.add(new Block(ser.row, ser.j, colors[r], ser.type));
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



        //TODO hit to break and some work here....
        //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
    }

    @Override
    public void onRender() {

    }


    @Override
    public void onInit() {

    }

    @Override
    public void stop() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }
    }


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

        //System.out.println("time is:" + time + " goldTime is " + goldTime);

    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
