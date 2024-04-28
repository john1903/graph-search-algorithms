package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public interface BoardValidatorInterface {
    boolean isMoveValid(Board board, int row, int column, char direction);

    boolean isBoardSolved(Board board);

    boolean isBoardSolvable(Board board);
}
