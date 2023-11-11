package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static final int BLOCK_NORMAL = 99;
    private static final int BLOCK_CHOCO = 100;
    private static final int BLOCK_STAR = 101;
    private static final int BLOCK_HEART = 102;
    private static final int NO_HIT = -1;
    private static final int HIT_RIGHT = 0;
    private static final int HIT_BOTTOM = 1;
    private static final int HIT_LEFT = 2;
    private static final int HIT_TOP = 3;

    private int row;
    private int column;
    private boolean isDestroyed = false;
    private Color color;
    private int type;
    private int x;
    private int y;
    private Rectangle rect;

    private static int width = 100;
    private static int height = 30;
    private static int paddingTop = height * 2;
    private static int paddingH = 50;

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        ImagePattern pattern = loadImagePatternBasedOnType(type);
        if (pattern != null) {
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }
    }

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

    // Getters and Setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
    //If it doesn't work. Try this ' return this.isDestroyed; '

    public void setDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
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

    //Fix: Add Getter methods for the constants

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

    // Public static getter methods for hit constants
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
