package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

/**
 * BoardController provides methods to perform tile movements on a Board object.
 */
public class BoardController implements BoardControllerInterface {

    /**
     * Moves a tile on the board in the specified direction.
     *
     * @param board     Board object on which the tile movement is performed.
     * @param row       Row index of the tile to be moved.
     * @param column    Column index of the tile to be moved.
     * @param direction Direction in which the tile should be moved ('U' for up, 'D' for down,
     *                  'L' for left, 'R' for right).
     * @return true if the tile was successfully moved, false otherwise.
     * @throws IllegalArgumentException if the row or column index is out of bounds for the board.
     */
    @Override
    public boolean moveTile(Board board, int row, int column, char direction) {
        if (row * board.getColumns() + column >= board.getRows() * board.getColumns() ||
                row * board.getColumns() + column < 0) {
            throw new IllegalArgumentException("Invalid index");
        }
        return switch (direction) {
            case 'U' -> {
                board.setFieldValue(row, column, board.getFieldValue(row - 1, column));
                board.setFieldValue(row - 1, column, 0);
                yield true;
            }
            case 'D' -> {
                board.setFieldValue(row, column, board.getFieldValue(row + 1, column));
                board.setFieldValue(row + 1, column, 0);
                yield true;
            }
            case 'L' -> {
                board.setFieldValue(row, column, board.getFieldValue(row, column - 1));
                board.setFieldValue(row, column - 1, 0);
                yield true;
            }
            case 'R' -> {
                board.setFieldValue(row, column, board.getFieldValue(row, column + 1));
                board.setFieldValue(row, column + 1, 0);
                yield true;
            }
            default -> false;
        };
    }
}