package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public class BoardValidator implements BoardValidatorInterface {
    @Override
    public boolean isMoveValid(Board board, int row, int column, char direction) {
        if (board.getFieldValue(row, column) != 0) {
            return false;
        }
        return switch (direction) {
            case 'U' -> row != 0;
            case 'D' -> row != board.getRows() - 1;
            case 'L' -> column != 0;
            case 'R' -> column != board.getColumns() - 1;
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }

    @Override
    public boolean isBoardSolved(Board board) {
        int rows = board.getRows();
        int columns = board.getColumns();
        for (int i = 0; i < rows * columns - 1; i++) {
            int x = i / columns;
            int y = i % columns;
            if (board.getFieldValue(x, y) != i + 1) {
                return false;
            }
        }
        return board.getFieldValue(rows - 1, columns - 1) == 0;
    }


    @Override
    public boolean isBoardSolvable(Board board) {
        return false;
    }
}
