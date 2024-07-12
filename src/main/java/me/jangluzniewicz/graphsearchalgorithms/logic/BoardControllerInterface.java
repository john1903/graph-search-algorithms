package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

/**
 * Interface for controlling tile movements on a board.
 */
public interface BoardControllerInterface {

    /**
     * Moves a tile on the board in the specified direction.
     *
     * @param board    The board on which the tile is being moved.
     * @param row      The row index of the tile to move.
     * @param column   The column index of the tile to move.
     * @param direction The direction in which to move the tile ('U' for up, 'D' for down, 'L' for left, 'R' for right).
     * @return True if the tile was moved successfully, false otherwise.
     */
    boolean moveTile(Board board, int row, int column, char direction);
}
