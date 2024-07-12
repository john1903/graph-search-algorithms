package me.jangluzniewicz.graphsearchalgorithms.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import me.jangluzniewicz.graphsearchalgorithms.model.Board;

/**
 * BoardWrapper wraps a Board object for use in graphical user interfaces,
 * providing properties and methods for interacting with the board.
 */
public class BoardWrapper {
    private final Board board;
    private final IntegerProperty[][] tileProperties;
    private final BooleanProperty isBoardSolved;

    /**
     * Constructs a BoardWrapper with the specified Board.
     *
     * @param board The Board object to wrap.
     */
    public BoardWrapper(Board board) {
        this.board = board;
        this.tileProperties = new SimpleIntegerProperty[board.getRows()][board.getColumns()];
        this.isBoardSolved = new SimpleBooleanProperty(board.isBoardSolved());

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                tileProperties[i][j] = new SimpleIntegerProperty(board.getFieldValue(i, j));
                final int row = i;
                final int col = j;
                tileProperties[i][j].addListener((obs, oldVal, newVal) -> {
                    board.setFieldValue(row, col, newVal.intValue());
                    isBoardSolved.set(board.isBoardSolved());
                });
            }
        }
    }

    /**
     * Retrieves the IntegerProperty for the tile at the specified row and column.
     *
     * @param row    Row index of the tile.
     * @param col    Column index of the tile.
     * @return The IntegerProperty associated with the tile.
     */
    public IntegerProperty tileProperty(int row, int col) {
        return tileProperties[row][col];
    }

    /**
     * Moves a tile on the board in the specified direction.
     *
     * @param row       Row index of the tile to move.
     * @param column    Column index of the tile to move.
     * @param direction Direction in which to move the tile ('U' for up, 'D' for down,
     *                  'L' for left, 'R' for right).
     */
    public void moveTile(int row, int column, char direction) {
        board.move(row, column, direction);
        updateProperties();
    }

    /**
     * Updates the IntegerProperties of tiles to reflect the current state of the board.
     */
    private void updateProperties() {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                tileProperties[i][j].set(board.getFieldValue(i, j));
            }
        }
        isBoardSolved.set(board.isBoardSolved());
    }

    /**
     * Retrieves the number of rows in the board.
     *
     * @return The number of rows in the board.
     */
    public int getRows() {
        return board.getRows();
    }

    /**
     * Retrieves the number of columns in the board.
     *
     * @return The number of columns in the board.
     */
    public int getColumns() {
        return board.getColumns();
    }

    /**
     * Retrieves the wrapped Board object.
     *
     * @return The wrapped Board object.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Retrieves the BooleanProperty indicating whether the board is solved.
     *
     * @return The BooleanProperty indicating board solved status.
     */
    public BooleanProperty isBoardSolvedProperty() {
        return isBoardSolved;
    }
}