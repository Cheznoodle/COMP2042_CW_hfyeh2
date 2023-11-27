package brickGame;

import java.io.Serializable;

/**
 * Serializable representation of a block in the brick game.
 * This class is designed for serialization and contains essential data
 * about a block's position and type.
 */
public class BlockSerializable implements Serializable {

    // Fields to store the block's position and type
    public final int row; // The row in the grid where the block is placed
    public final int j;   // The column in the grid where the block is placed
    public final int type; // The type of the block

    /**
     * Constructs a serializable version of a block.
     *
     * @param row  The row in the grid where the block is located.
     * @param j    The column in the grid where the block is located.
     * @param type The type of the block.
     */
    public BlockSerializable(int row, int j, int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
