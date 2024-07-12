package me.jangluzniewicz.graphsearchalgorithms.model;

import me.jangluzniewicz.graphsearchalgorithms.logic.BoardControllerInterface;
import me.jangluzniewicz.graphsearchalgorithms.logic.BoardValidatorInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a board in a 15 puzzle with a grid of fields.
 */
public class Board implements Cloneable {
    private final List<Field> fields;
    BoardControllerInterface boardController;
    BoardValidatorInterface boardValidator;
    private final int rows;
    private final int columns;

    /**
     * Constructs a Board with a 4x4 grid of fields.
     *
     * @param fieldsArray      An ArrayList of Field objects representing the fields on the board.
     * @param boardController  An implementation of BoardControllerInterface for handling board actions.
     * @param boardValidator   An implementation of BoardValidatorInterface for validating board state and moves.
     */
    public Board(ArrayList<Field> fieldsArray, BoardControllerInterface boardController,
                 BoardValidatorInterface boardValidator) {
        fields = new ArrayList<>();
        rows = 4;
        columns = 4;
        if (fieldsArray.size() != rows * columns) {
            throw new IllegalArgumentException("Board must have 16 fields");
        }
        for (int i = 0; i < rows * columns; i++) {
            fields.add(new Field(fieldsArray.get(i).getValue()));
        }
        this.boardController = boardController;
        this.boardValidator = boardValidator;
    }

    /**
     * Constructs a Board with specified number of rows and columns.
     *
     * @param rows             The number of rows on the board.
     * @param columns          The number of columns on the board.
     * @param fieldsArray      An ArrayList of Field objects representing the fields on the board.
     * @param boardController  An implementation of BoardControllerInterface for handling board actions.
     * @param boardValidator   An implementation of BoardValidatorInterface for validating board state and moves.
     */
    public Board(int rows, int columns, ArrayList<Field> fieldsArray, BoardControllerInterface boardController,
                 BoardValidatorInterface boardValidator) {
        fields = new ArrayList<>();
        this.rows = rows;
        this.columns = columns;
        if (fieldsArray.size() != rows * columns) {
            throw new IllegalArgumentException("Incorrect number of fields");
        }
        for (int i = 0; i < rows * columns; i++) {
            fields.add(new Field(fieldsArray.get(i).getValue()));
        }
        this.boardController = boardController;
        this.boardValidator = boardValidator;
    }

    /**
     * Gets the value of a field at the specified position.
     *
     * @param x The row index of the field.
     * @param y The column index of the field.
     * @return The value of the field at the specified position.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    public int getFieldValue(int x, int y) {
        if (x >= rows || y >= columns || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid index");
        }
        return fields.get(x * columns + y).getValue();
    }

    /**
     * Sets the value of a field at the specified position.
     *
     * @param x     The row index of the field.
     * @param y     The column index of the field.
     * @param value The new value to set for the field.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    public void setFieldValue(int x, int y, int value) {
        if (x >= rows || y >= columns || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid index");
        }
        fields.get(x * columns + y).setValue(value);
    }

    /**
     * Returns a string representation of the board.
     *
     * @return A string representing the board.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                stringBuilder.append(fields.get(i * columns + j).getValue());
                if (j < columns - 1) {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Computes the hash code for the board.
     *
     * @return The hash code of the board.
     */
    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(fields);
        return hashCodeBuilder.toHashCode();
    }

    /**
     * Checks if this board is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Board rhs = (Board) obj;
        equalsBuilder.append(fields, rhs.fields);
        return equalsBuilder.isEquals();
    }

    /**
     * Gets the number of rows on the board.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns on the board.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Moves a tile on the board in the specified direction.
     *
     * @param row       The row index of the tile to move.
     * @param column    The column index of the tile to move.
     * @param direction The direction to move the tile ('U' for up, 'D' for down, 'L' for left, 'R' for right).
     * @return True if the move was successful, false otherwise.
     */
    public boolean move(int row, int column, char direction) {
        if (boardValidator.isMoveValid(this, row, column, direction)) {
            return boardController.moveTile(this, row, column, direction);
        }
        return false;
    }

    /**
     * Finds the position of the empty field (value 0) on the board.
     *
     * @return A list containing the row and column indices of the empty field.
     * @throws IllegalArgumentException if no empty field is found.
     */
    public List<Integer> getEmptyPosition() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (getFieldValue(i, j) == 0) {
                    return new ArrayList<>(List.of(i, j));
                }
            }
        }
        throw new IllegalArgumentException("No empty field");
    }

    /**
     * Gets the possible moves for a tile at the specified position.
     *
     * @param row    The row index of the tile.
     * @param column The column index of the tile.
     * @return A list of characters representing the possible moves ('U', 'D', 'L', 'R').
     */
    public List<Character> getPossibleMoves(int row, int column) {
        List<Integer> emptyPosition = this.getEmptyPosition();
        List<Character> possibleMoves = new ArrayList<>();

        int emptyRow = emptyPosition.get(0);
        int emptyColumn = emptyPosition.get(1);

        if (row == emptyRow && column == emptyColumn) {
            if (boardValidator.isMoveValid(this, row, column, 'U')) {
                possibleMoves.add('U');
            }
            if (boardValidator.isMoveValid(this, row, column, 'D')) {
                possibleMoves.add('D');
            }
            if (boardValidator.isMoveValid(this, row, column, 'L')) {
                possibleMoves.add('L');
            }
            if (boardValidator.isMoveValid(this, row, column, 'R')) {
                possibleMoves.add('R');
            }
        }
        return possibleMoves;
    }

    /**
     * Creates and returns a copy of this board.
     *
     * @return A clone of this board.
     */
    @Override
    public Object clone() {
        ArrayList<Field> clonedFields = new ArrayList<>();
        for (Field field : fields) {
            clonedFields.add(new Field(field.getValue()));
        }
        return new Board(rows, columns, clonedFields, boardController, boardValidator);
    }

    /**
     * Checks if the board is in a solved state.
     *
     * @return True if the board is solved, false otherwise.
     */
    public boolean isBoardSolved() {
        return boardValidator.isBoardSolved(this);
    }
}
