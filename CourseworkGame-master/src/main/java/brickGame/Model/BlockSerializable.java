package brickGame.Model;

import java.io.Serializable;

/**
 * Represents a serializable version of a block in a brick-breaking game.
 * This class is designed to facilitate the serialization process and stores essential information
 * about the block, including its position, type, and color. It is used for saving the game state.
 */
public class BlockSerializable implements Serializable {

    // Fields to store the block's position, type, and color
    public final int row; // The row in the grid where the block is placed
    public final int j;   // The column in the grid where the block is placed
    public final int type; // The type of the block (e.g., normal, choco, star, heart)
    public final String colorString; // The color of the block, represented as a string


    /**
     * Constructs a serializable version of a block with specified attributes.
     * This constructor initializes the block with its grid position, type, and color.
     *
     * @param row         The row in the grid where the block is located.
     * @param j           The column in the grid where the block is located.
     * @param type        The type of the block, determining its behavior and appearance.
     * @param colorString The color of the block represented as a string for serialization purposes.
     */
    public BlockSerializable(int row, int j, int type, String colorString) {
        this.row = row;
        this.j = j;
        this.type = type;
        this.colorString = colorString;
    }
}
