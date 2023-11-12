package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadSave {
    // Logger for logging error messages
    private static final Logger LOGGER = Logger.getLogger(LoadSave.class.getName());

    // Fields to store the game state
    public boolean isExistHeartBlock;
    public boolean isGoldStatus;
    public boolean goDownBall;
    public boolean goRightBall;
    public boolean collideToBreak;
    public boolean collideToBreakAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;
    public int level;
    public int score;
    public int heart;
    public int destroyedBlockCount;
    public double xBall;
    public double yBall;
    public double xBreak;
    public double yBreak;
    public double centerBreakX;
    public long time;
    public long goldTime;
    public double vX;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();

    /**
     * Reads the game state from a file.
     */
    public void read() {
        // Prepare the file to read from
        File file = new File(Main.savePath);

        // Use try-with-resources for automatic resource management
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            // Read and assign values from the file to the fields
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

            // Cast the read object to the expected ArrayList type
            blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            // Log class not found exceptions
            LOGGER.log(Level.SEVERE, "Class not found while reading from the file", e);
        } catch (IOException e) {
            // Log IO exceptions
            LOGGER.log(Level.SEVERE, "IO Exception while reading from the file", e);
        }
    }
}

//Note: Possible Future Feature: Configure Logger for specific needs (eg:setting a different logging level, formatting the output, or directing the log messages to a file.)