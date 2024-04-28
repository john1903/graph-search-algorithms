package me.jangluzniewicz.graphsearchalgorithms.model;

import me.jangluzniewicz.graphsearchalgorithms.logic.BoardControllerInterface;
import me.jangluzniewicz.graphsearchalgorithms.logic.BoardValidatorInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private final List<Field> fields;
    BoardControllerInterface boardController;
    BoardValidatorInterface boardValidator;
    private final int rows;
    private final int columns;

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

    public int getFieldValue(int x, int y) {
        if (x >= rows || y >= columns || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid index");
        }
        return fields.get(x * columns + y).getValue();
    }

    public void setFieldValue(int x, int y, int value) {
        if (x >= rows || y >= columns || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid index");
        }
        fields.get(x * columns + y).setValue(value);
    }

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

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(fields);
        return hashCodeBuilder.toHashCode();
    }

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

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean move(int row, int column, char direction) {
        if (boardValidator.isMoveValid(this, row, column, direction)) {
            return boardController.moveTile(this, row, column, direction);
        }
        return false;
    }

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

    @Override
    public Object clone() {
        ArrayList<Field> clonedFields = new ArrayList<>();
        for (Field field : fields) {
            clonedFields.add(new Field(field.getValue()));
        }
        return new Board(rows, columns, clonedFields, boardController, boardValidator);
    }

    public boolean isBoardSolved() {
        return boardValidator.isBoardSolved(this);
    }
}
