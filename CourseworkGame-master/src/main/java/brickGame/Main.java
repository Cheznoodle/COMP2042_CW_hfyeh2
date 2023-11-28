package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import javafx.animation.FadeTransition;


public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {


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

    private final ArrayList<Block> blocks = new ArrayList<Block>();
    private final ArrayList<Bonus> chocos = new ArrayList<Bonus>();
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

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
        if (!loadFromSave) {
            //root.getChildren().addAll(load, newGame);
        }

        for (Block block : blocks) {
            root.getChildren().add(block.getRect());
        }

        Scene scene = new Scene(root, sceneWidth, sceneHeight);

        // Load the CSS file
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

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
            load.setLayoutY(sceneHeight / 2 - 50); // Vertical positioning

            newGame.setLayoutX((sceneWidth - newGameButtonWidth) / 2);
            newGame.setLayoutY(sceneHeight / 2 + 10); // Vertical positioning

            exitGame.setLayoutX((sceneWidth - exitButtonWidth) / 2);
            exitGame.setLayoutY(sceneHeight / 2 + 70);
        });

        if (loadFromSave == false) {
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

            exitGame.setOnAction(event -> {
                Platform.exit();
            });

        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }

    }

    private void shakeStage() {
        final double originalX = primaryStage.getX();
        final double originalY = primaryStage.getY();
        final int shakeDistance = 5;
        final int shakeCycles = 10;

        for (int i = 0; i < shakeCycles; i++) {
            Platform.runLater(() -> {
                primaryStage.setX(originalX + Math.random() * shakeDistance - shakeDistance / 2);
                primaryStage.setY(originalY + Math.random() * shakeDistance - shakeDistance / 2);
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

    private void showHeartDeductedImage() {
        Platform.runLater(() -> {
            // Create an ImageView and attempt to load the image
            Image image = new Image("minusHeart.png", 200, 200, true, true);
            ImageView heartImage = new ImageView(image);

            // Check if the image has been loaded correctly
            if (image.isError()) {
                System.out.println("Error loading heart image");
                return; // Exit if the image hasn't been loaded correctly
            }

            VBox imageContainer = new VBox(heartImage);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefSize(sceneWidth, sceneHeight);

            // Initially set the image to be transparent
            imageContainer.setOpacity(0);

            root.getChildren().add(imageContainer);

            // Fade in transition
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), imageContainer);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), imageContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.setDelay(Duration.seconds(1)); // Delay to keep the image visible

            // Start fade in transition
            fadeIn.play();

            // After fade in, start fade out
            fadeIn.setOnFinished(event -> fadeOut.play());

            // After fade out, remove the image container
            fadeOut.setOnFinished(event -> root.getChildren().remove(imageContainer));
        });
    }


    //REFACTOR BELOW
    //Fix: Replaced the direct access to the Block constants with the appropriate getter methods
    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
//                if (r % 5 == 0) {
//                    continue;
//                }
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
        }
    }
    //REFACTOR ABOVE


    //REFACTOR HERE
    private void move(final int direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return;
                    }
                    if (xBreak == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    centerBreakX = xBreak + halfBreakWidth;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();


    }
    //REFACTOR ABOVE

    //REFACTOR HER
    //Background Sound Method
    private void playBackgroundSound() {
        if (backgroundMediaPlayer == null) { // Only initialize if not already done
            try {
                URL resource = getClass().getResource("/wii.mp3");
                if (resource == null) {
                    System.err.println("Audio file not found!");
                    return;
                }
                Media media = new Media(resource.toString());
                backgroundMediaPlayer = new MediaPlayer(media);
                backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
                backgroundMediaPlayer.play();
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions
            }
        }
    }
    //REFACTOR ABOVE

    public static void main(String[] args) {
        launch(args);
    }

    //REFACTOR DONE
    private void initBall() {
        // Set fixed coordinates for the ball's initial position
        xBall = sceneWidth / 2.0; // Spawn in the middle of the scene width-wise
        yBall = sceneHeight / 2.0; // Spawn in the middle of the scene height-wise

        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(loadImagePattern("ball.png"));
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);
        rect.setFill(loadImagePattern("block.png"));
    }

    private ImagePattern loadImagePattern(String imagePath) {
        return new ImagePattern(new Image(imagePath));
    }
    //REFACTOR DONE

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
                shakeStage();
                showHeartDeductedImage();
                new Score().show(sceneWidth / 2, sceneHeight / 2, -1, this);

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

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

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

                if (xBall - centerBreakX > 0) {
                    collideToBreakAndMoveToRight = true;
                } else {
                    collideToBreakAndMoveToRight = false;
                }
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
            if (collideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

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

                    //Fix: Getter method for the isDestroyed
                    ArrayList<BlockSerializable> blockSerializable = new ArrayList<BlockSerializable>();
                    for (Block block : blocks) {
                        if (block.isDestroyed()) {  // Updated to use the getter method
                            continue;
                        }
                        blockSerializable.add(new BlockSerializable(block.getRow(), block.getColumn(), block.getType()));
                    }


                    outputStream.writeObject(blockSerializable);

                    new Score().showMessage("Game Saved", Main.this);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToBreak = loadSave.collideToBreak;
        collideToBreakAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
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
                    }

                    if (block.getType() == Block.getBlockHeart()) {
                        heart++;
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
