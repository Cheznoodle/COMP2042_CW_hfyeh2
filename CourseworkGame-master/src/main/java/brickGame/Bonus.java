package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;
import java.util.Random;

/**
 * Represents a bonus object in the brick game.
 * This class handles the creation and visual representation of bonuses
 * that appear during the game.
 */
public class Bonus implements Serializable {
    // Constants for the size of the bonus and the offset in positioning
    private static final int CHOCO_SIZE = 30;
    private static final int OFFSET = 15;

    // Static random number generator for random image selection
    private static final Random rand = new Random();

    // The graphical representation of the bonus
    public Rectangle choco;

    // Positional attributes for the bonus
    public double x;
    public double y;

    // Time when the bonus was created
    public long timeCreated;

    // Flag to check if the bonus has been taken
    public boolean taken = false;

    /**
     * Constructs a new Bonus object at a specified location on the game grid.
     * The position is calculated based on the row and column, and the bonus
     * is visually represented by a rectangle.
     *
     * @param row    The row in the game grid where the bonus is located.
     * @param column The column in the game grid where the bonus is located.
     */
    public Bonus(int row, int column) {
        // Calculate the position of the bonus based on the row and column
        x = (column * Block.getWidth()) + Block.getPaddingH() + (Block.getWidth() / 2.0) - OFFSET;
        y = (row * Block.getHeight()) + Block.getPaddingTop() + (Block.getHeight() / 2.0) - OFFSET;

        // Call the method to draw the bonus
        draw();
    }

    /**
     * Initializes and draws the graphical representation of the bonus.
     * Sets the position, size, and image of the bonus based on predefined parameters.
     */
    private void draw() {
        // Initialize the rectangle representing the bonus
        choco = new Rectangle();
        choco.setWidth(CHOCO_SIZE);
        choco.setHeight(CHOCO_SIZE);
        choco.setX(x);
        choco.setY(y);

        // Select a random image for the bonus
        String url = rand.nextInt(20) % 2 == 0 ? "bonus1.png" : "bonus2.png";

        // Set the image of the bonus, handling any errors in image loading
        try {
            choco.setFill(new ImagePattern(new Image(url)));
        } catch (IllegalArgumentException e) {
            System.err.println("Error loading image: " + url);
        }
    }
}
