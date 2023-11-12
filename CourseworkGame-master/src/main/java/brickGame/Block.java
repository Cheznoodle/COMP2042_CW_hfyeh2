package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Represents a block in the brick game, which can have different types and be hit by a ball.
 */

public class Block implements Serializable {
    // Constants to represent different types of blocks and hit outcomes
    private static final int BLOCK_NORMAL = 99;
    private static final int BLOCK_CHOCO = 100;
    private static final int BLOCK_STAR = 101;
    private static final int BLOCK_HEART = 102;
    private static final int NO_HIT = -1;
    private static final int HIT_RIGHT = 0;
    private static final int HIT_BOTTOM = 1;
    private static final int HIT_LEFT = 2;
    private static final int HIT_TOP = 3;

    // Properties defining the block's position, appearance, and state
    private final int row;
    private final int column;
    private boolean isDestroyed = false;
    private final Color color;
    private final int type;
    private int x;
    private int y;
    private Rectangle rect;

    // Fixed dimensions and padding for all blocks
    private static final int width = 100;
    private static final int height = 30;
    private static final int paddingTop = height * 2;
    private static final int paddingH = 50;

    /**
     * Constructs a new Block with specified parameters.
     *
     * @param row    The row in the grid where the block is placed.
     * @param column The column in the grid where the block is placed.
     * @param color  The color of the block.
     * @param type   The type of the block, determining its behavior.
     */

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    /**
     * Draws the block, setting up its visual representation.
     */

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        // Initialize and set properties of the rectangle representing the block
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        // Set the fill pattern based on the block's type
        ImagePattern pattern = loadImagePatternBasedOnType(type);
        if (pattern != null) {
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }
    }

    /**
     * Loads an image pattern based on the block's type.
     *
     * @param type The type of the block.
     * @return An ImagePattern to fill the block, or null if there's no specific pattern.
     */

    private ImagePattern loadImagePatternBasedOnType(int type) {
        String imagePath;
        switch (type) {
            case BLOCK_CHOCO:
                imagePath = "choco.jpg";
                break;
            case BLOCK_HEART:
                imagePath = "heart.jpg";
                break;
            case BLOCK_STAR:
                imagePath = "star.jpg";
                break;
            default:
                return null;
        }
        Image image = new Image(imagePath);
        return new ImagePattern(image);
    }

    /**
     * Checks if a ball hits this block.
     *
     * @param xBall The x-coordinate of the ball.
     * @param yBall The y-coordinate of the ball.
     * @return An integer representing the side of the block that was hit.
     */

    public int checkHitToBlock(double xBall, double yBall) {
        if (isDestroyed) {
            return NO_HIT;
        }

        if (xBall >= x && xBall <= x + width && yBall == y + height) {
            return HIT_BOTTOM;
        }

        if (xBall >= x && xBall <= x + width && yBall == y) {
            return HIT_TOP;
        }

        if (yBall >= y && yBall <= y + height && xBall == x + width) {
            return HIT_RIGHT;
        }

        if (yBall >= y && yBall <= y + height && xBall == x) {
            return HIT_LEFT;
        }

        return NO_HIT;
    }

    // Getters and Setters for the block's properties
    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    public boolean isDestroyed() {
        return isDestroyed;
    }


    public void setDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }


    public int getType() {
        return type;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public Rectangle getRect() {
        return rect;
    }


    public static int getPaddingTop() {
        return paddingTop;
    }

    public static int getPaddingH() {
        return paddingH;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    //Additional static getter methods for class constants
    public static int getBlockNormal() {
        return BLOCK_NORMAL;
    }

    public static int getBlockChoco() {
        return BLOCK_CHOCO;
    }

    public static int getBlockStar() {
        return BLOCK_STAR;
    }

    public static int getBlockHeart() {
        return BLOCK_HEART;
    }

    public static int getNoHit() {
        return NO_HIT;
    }

    public static int getHitRight() {
        return HIT_RIGHT;
    }

    public static int getHitBottom() {
        return HIT_BOTTOM;
    }

    public static int getHitLeft() {
        return HIT_LEFT;
    }

    public static int getHitTop() {
        return HIT_TOP;
    }

}
