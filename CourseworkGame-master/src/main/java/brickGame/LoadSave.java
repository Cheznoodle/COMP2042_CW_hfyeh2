package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for loading the saved game state from a file.
 */
public class LoadSave {
    // Logger for recording any errors that occur during the file loading process.
    private static final Logger LOGGER = Logger.getLogger(LoadSave.class.getName());

    // Fields representing the different aspects of the game's state.
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
    public ArrayList<BlockSerializable> blocks;

    /**
     * Reads the saved game state from a file.
     */
    public void read() {
        File file = new File(Main.savePath);
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
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

            // Safe type casting with validation
            Object readObject = inputStream.readObject();
            if (readObject instanceof ArrayList<?> tempList) {
                if (!tempList.isEmpty() && tempList.get(0) instanceof BlockSerializable) {
                    blocks = (ArrayList<BlockSerializable>) tempList;
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found in LoadSave", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception in LoadSave", e);
        }
    }
}
