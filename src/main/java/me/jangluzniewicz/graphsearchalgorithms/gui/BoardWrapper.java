package me.jangluzniewicz.graphsearchalgorithms.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public class BoardWrapper {
    private final Board board;
    private final IntegerProperty[][] tileProperties;
    private final BooleanProperty isBoardSolved;

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

    public IntegerProperty tileProperty(int row, int col) {
        return tileProperties[row][col];
    }

    public void moveTile(int row, int column, char direction) {
        board.move(row, column, direction);
        updateProperties();
    }

    private void updateProperties() {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                tileProperties[i][j].set(board.getFieldValue(i, j));
            }
        }
        isBoardSolved.set(board.isBoardSolved());
    }

    public int getRows() {
        return board.getRows();
    }

    public int getColumns() {
        return board.getColumns();
    }

    public Board getBoard() {
        return board;
    }

    public BooleanProperty isBoardSolvedProperty() {
        return isBoardSolved;
    }
}
