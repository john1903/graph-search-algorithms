package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public class BoardController implements BoardControllerInterface {
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
